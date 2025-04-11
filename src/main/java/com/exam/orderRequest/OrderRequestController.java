package com.exam.orderRequest;

import com.exam.cartAnalysis.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orderRequest")
public class OrderRequestController {

    @Autowired
    private OrderRequestService orderRequestService;

    @Autowired
    private OrdersRepository ordersRepository;


    // 👉 발주 요청 등록 API
    @PostMapping("/request")
    public ResponseEntity<?> requestOrder(@RequestBody Map<String, Object> payload) {
        Long goodsId = Long.parseLong(payload.get("goodsId").toString());
        Long quantity = Long.parseLong(payload.get("addStock").toString());

        orderRequestService.placeOrder(goodsId, quantity);
        return ResponseEntity.ok("발주 요청 완료");
    }


    // 발주 리스트 조회
    @GetMapping("/list")
    public ResponseEntity<List<OrderRequestDTO>> getAllOrders() {
        List<OrderRequestDTO> orders = orderRequestService.getAllOrders();
        return ResponseEntity.status(200).body(orders);
    }


    // 가장 최신 발주 1건 조회
    @GetMapping("/latest/{goodsId}")
    public ResponseEntity<OrderRequestDTO> getLatestOrder(@PathVariable Long goodsId) {
        OrderRequestDTO orderRequestDTO = orderRequestService.findTop1ByGoodsOrderByScheduledTimeDesc(goodsId);
        return ResponseEntity.status(200).body(orderRequestDTO);
    }


    // 발주 상태 업데이트
    @PostMapping("/confirm/{orderId}")
    public ResponseEntity<String> confirmOrder(
            @PathVariable Long orderId
    ) {
        orderRequestService.confirmOrder(orderId);
        return ResponseEntity.status(200).body("상태 변환 완료");
    }

}
