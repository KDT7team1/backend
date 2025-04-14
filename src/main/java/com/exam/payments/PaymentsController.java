package com.exam.payments;

import com.exam.Inventory.InventoryService;
import com.exam.alert.SseService;
import com.exam.cartAnalysis.dto.OrdersDTO;
import com.exam.cartAnalysis.entity.Orders;
import com.exam.cartAnalysis.repository.OrdersRepository;
import com.exam.cartAnalysis.service.OrdersService;
import com.exam.goods.Goods;
import com.exam.goods.GoodsRepository;
import com.exam.saleData.SaleData;
import com.exam.saleData.SaleDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentsController {

    private final PaymentsService paymentsService;
    private final OrdersService ordersService;
    private final OrdersRepository ordersRepository;
    private final GoodsRepository goodsRepository;
    private final SaleDataRepository saleDataRepository;
    private final InventoryService inventoryService;
    private final SseService sseService;

    @PostMapping("/order")
    public ResponseEntity<Map<String, Long>> createOrder(@RequestBody OrdersDTO ordersDTO) {
        log.info("LOGGER: [PAYMENT] 주문 요청, orders 테이블에 주문내역 추가");
        Long orderId = ordersService.createOrder(ordersDTO);
        Map<String, Long> response = new HashMap<>();
        response.put("orderId", orderId);
        log.info("LOGGER: [PAYMENT] 새로운 주문내역 추가: {}", orderId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm")
    public ResponseEntity<JSONObject> confirmPayment(@RequestBody String jsonBody) throws Exception {
        log.info("LOGGER: [PAYMENT] Toss Pay 요청함");

        JSONParser parser = new JSONParser();

        String orderId;
        String amount;
        String paymentKey;

        try {
            JSONObject requestData = (JSONObject) parser.parse(jsonBody);
            paymentKey = (String) requestData.get("paymentKey");
            orderId = (String) requestData.get("orderId");
            amount = (String) requestData.get("amount");
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Invalid request data"));
        }

        OrdersDTO orders = ordersService.findById(Long.parseLong(orderId));

        // 중복 결제 방지
        if (orders.getPaymentStatus() == PaymentStatus.COMPLETED) {
            log.info("[TOSS PAY] 예외 발생 - 이미 결제된 주문");
            throw new IllegalArgumentException("이미 결제된 주문입니다.");
        }

        JSONObject obj = new JSONObject();
        obj.put("orderId", orderId);
        obj.put("amount", Integer.parseInt(amount));
        obj.put("paymentKey", paymentKey);

        String widgetSecretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);

        URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));

        int code = connection.getResponseCode();
        boolean isSuccess = code == 200;

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();
        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONObject jsonObject = (JSONObject) parser.parse(reader);

        if (isSuccess) {
            String approvedAtStr = (String) jsonObject.get("approvedAt");
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(approvedAtStr);
            LocalDateTime approvedAt = offsetDateTime.toLocalDateTime();

            log.info("[TOSS PAY] 결제 성공함 payment 테이블에 저장");
            PaymentsDTO dto = PaymentsDTO.builder()
                    .ordersId(Long.parseLong(orderId))
                    .memberNo(orders.getMemberNo())
                    .finalPrice(orders.getFinalPrice())
                    .paymentAmount(Long.parseLong(amount))
                    .paymentMethod((String) jsonObject.get("method"))
                    .paymentApproved(approvedAt)
                    .build();

            paymentsService.savePayment(dto);

            log.info("[TOSS PAY] 결제 성공함 paymentStatus 업데이트");
            ordersService.updatePaymentStatus(Long.parseLong(orderId), PaymentStatus.COMPLETED);
            ordersRepository.flush();

            sseService.sendNotification("admin", "결제", "✅ 결제가 완료되었습니다.");
            log.info("결제 알림 보내기");

            if (orders.getOrderItems() == null) {
                log.warn("[TOSS PAY] 주문 상세(orderItems)가 null입니다.");
            } else {
                for (OrdersDTO.OrderItemDTO item : orders.getOrderItems()) {
                    log.info("[TOSS PAY] 재고 차감 대상 상품 - goodsId: {}, saleAmount: {}", item.getGoodsId(), item.getSaleAmount());
                    inventoryService.reduceStock(item.getGoodsId(), item.getSaleAmount().longValue());
                }
                log.info("[TOSS PAY] 결제 성공 → 재고 감소 처리 완료");
            }

            responseStream.close();
            return ResponseEntity.status(code).body(jsonObject); // ✅ 성공 후 바로 return
        }

        // ❌ 실패일 때만 sale_data 삭제 로직 실행
//        Orders order = ordersRepository.findById(Long.parseLong(orderId)).orElse(null);
//        if (order != null && order.getPaymentStatus() != PaymentStatus.COMPLETED) {
//            saleDataRepository.deleteByOrders_OrdersId(order.getOrdersId());
//            log.info("❌ 결제 실패로 sale_data 삭제됨: orderId = {}", orderId);
//        } else {
//            log.info("✅ 이미 결제 성공된 주문 - sale_data 삭제하지 않음: orderId = {}", orderId);
//        }

        responseStream.close();
        return ResponseEntity.status(code).body(jsonObject);
    }

    // 에러 응답을 위한 메서드
    private JSONObject createErrorResponse(String message) {
        JSONObject errorResponse = new JSONObject();
        errorResponse.put("error", message);
        return errorResponse;
    }


}