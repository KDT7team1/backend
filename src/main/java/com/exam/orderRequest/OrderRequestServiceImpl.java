package com.exam.orderRequest;



import com.exam.Inventory.InventoryService;
import com.exam.cartAnalysis.repository.OrdersRepository;
import com.exam.goods.Goods;
import com.exam.goods.GoodsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class OrderRequestServiceImpl implements OrderRequestService {


    private final InventoryService inventoryService;
    OrderRequestRepository orderRequestRepository;
     GoodsRepository goodsRepository;

     public OrderRequestServiceImpl (OrderRequestRepository orderRequestRepository, GoodsRepository goodsRepository, OrdersRepository ordersRepository, InventoryService inventoryService)
     {
         this.goodsRepository = goodsRepository;
         this.orderRequestRepository = orderRequestRepository;
         this.inventoryService = inventoryService;
     }

     // 발주 요청 저장하는 로직 =>  "입고 예약표"
    @Override
    public void placeOrder(Long goodsId, Long quantity) {
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        OrderRequest order = OrderRequest.builder()
                .goods(goods)
                .orderQuantity(quantity)
                .orderTime(LocalDateTime.now())
                .status("대기")
                .scheduledTime(LocalDateTime.now().plusSeconds(5))
                .build();

        orderRequestRepository.save(order);

        new Thread(() -> {
            try {
                Thread.sleep(10_000); // 10초 대기
                inventoryService.addStock(
                        goods.getGoods_id(),
                        quantity,
                        LocalDateTime.now().plusDays(7) // 유통기한 임의 지정
                );
                order.setStatus("입고완료");
                orderRequestRepository.save(order);
                System.out.println("✅ 자동 입고 처리 완료: " + order.getOrderId());
            } catch (Exception e) {
                System.out.println("❌ 입고 처리 실패: " + e.getMessage());
                e.printStackTrace();
            }
        }).start(); // 🔻 별도 스레드로 실행 (메인 흐름 차단 방지)
     }

     // 발주 요청 리스트 전체 조회
    @Override
    public List<OrderRequest> getAllOrders() {
        return orderRequestRepository.findAll();
    }

    // 발주 요청 처리 후 완료 로직
    @Override
    public void completeOrder(Long orderId) {
        OrderRequest order = orderRequestRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("발주 내역을 찾을 수 없습니다."));

        order.setStatus("입고완료");
        orderRequestRepository.save(order);
    }
}
