package com.exam.orderRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderRequestController {

    @Autowired
    private OrderRequestService orderRequestService;

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
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderRequestService.getAllOrders();
        return ResponseEntity.status(200).body(orders);
    }


}
