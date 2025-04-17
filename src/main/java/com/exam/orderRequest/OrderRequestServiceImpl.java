package com.exam.orderRequest;



import com.exam.Inventory.InventoryService;
import com.exam.cartAnalysis.repository.OrdersRepository;
import com.exam.goods.Goods;
import com.exam.goods.GoodsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
                .status("발주 진행중")
                .scheduledTime(LocalDateTime.now().plusSeconds(5))
                .build();

        orderRequestRepository.save(order);

        // 3분 뒤 → '발주완료' 로만 변경 (재고 추가 X)
        new Thread(() -> {
            try {
                Thread.sleep(5000); // 3분 대기
                order.setStatus("발주완료");
                orderRequestRepository.save(order);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start(); // 🔻 별도 스레드로 실행 (메인 흐름 차단 방지)
     }

     // 발주 요청 리스트 전체 조회
    @Override
    public List<OrderRequestDTO> getAllOrders() {

         List<OrderRequestDTO> orders = orderRequestRepository.findAll().stream()
                 .map((item) -> {
                     OrderRequestDTO order = OrderRequestDTO.builder()
                             .orderId(item.getOrderId())
                             .goodsId(item.getGoods().getGoods_id())
                             .goodsName(item.getGoods().getGoods_name())
                             .goodsImage(item.getGoods().getGoods_image()) // 발주 리스트의 이미지 출력을 위해 추가
                             .orderQuantity(item.getOrderQuantity())
                             .orderTime(item.getOrderTime())
                             .status(item.getStatus())
                             .scheduledTime(item.getScheduledTime())
                             .build();
                     return order;
                 }).collect(Collectors.toList());

     return orders;
    }




    // 발주 요청 처리 후 완료 로직
    @Override
    @Transactional
    public void confirmOrder(Long orderId) {
        log.info("🔔 confirmOrder 진입: orderId={}", orderId);
        OrderRequest orderRequest = orderRequestRepository.findById(orderId)
                .orElseThrow();


        // 재고 입고
        inventoryService.addStock(
                orderRequest.getGoods().getGoods_id(),
                orderRequest.getOrderQuantity()
        );
        log.info("✅ addStock 호출 완료");

        orderRequest.setStatus("입고완료");
        orderRequestRepository.save(orderRequest);

        log.info("✅ 상태 '입고완료' 저장 완료");
    }

    // 상품별
    @Override
    public OrderRequestDTO findTop1ByGoodsOrderByScheduledTimeDesc(Long goodsId) {
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다."));

        OrderRequest orderRequest = orderRequestRepository.findTop1ByGoodsOrderByScheduledTimeDesc(goods);

       if(orderRequest == null){
           return null;
       }

       OrderRequestDTO orderRequestDTO = OrderRequestDTO.builder()
               .orderId(orderRequest.getOrderId())
               .goodsId(orderRequest.getGoods().getGoods_id())
               .goodsName(orderRequest.getGoods().getGoods_name())
               .goodsImage(orderRequest.getGoods().getGoods_image()) // 발주 리스트의 이미지 출력을 위해 추가
               .orderQuantity(orderRequest.getOrderQuantity())
               .orderTime(orderRequest.getOrderTime())
               .status(orderRequest.getStatus())
               .scheduledTime(orderRequest.getScheduledTime())
               .build();

       return orderRequestDTO;
    }


}
