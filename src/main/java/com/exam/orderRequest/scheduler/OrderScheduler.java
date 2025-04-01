//package com.exam.orderRequest.scheduler;
//
//import com.exam.Inventory.InventoryService;
//import com.exam.orderRequest.OrderRequest;
//import com.exam.orderRequest.OrderRequestRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class OrderScheduler {
//
//
//    @Autowired
//    private OrderRequestRepository orderRequestRepository;
//
//    @Autowired
//    private InventoryService inventoryService;
//
//
//
//    // 30초마다 자동 업데이트
//    @Scheduled(fixedRate = 3000)
//    @Transactional
//    public void scheduler() {
//        LocalDateTime now = LocalDateTime.now();
//        List<OrderRequest> readyOrders
//                = orderRequestRepository.findByStatusAndScheduledTimeBefore("대기중", now);
//
//        for (OrderRequest order : readyOrders) {
//            System.out.println("▶️ 처리 대상: " + order.getOrderId() + ", 상품 ID: " + order.getGoods().getGoods_id());
//        }
//
//        for(OrderRequest order : readyOrders) {
//            try{
//                inventoryService.addStock(
//                        order.getGoods().getGoods_id(),
//                        order.getOrderQuantity(),
//                        order.getScheduledTime().plusDays(7) // 유통기한 임의 설정
//                );
//                order.setStatus("입고완료");
//                orderRequestRepository.save(order);
//                System.out.println("✅ 자동 입고 처리 완료: " + order.getOrderId());
//            }catch (Exception e){
//                System.out.println("❌ 입고 처리 실패: " + e.getMessage());
//            }
//        }
//    }
//
//}
