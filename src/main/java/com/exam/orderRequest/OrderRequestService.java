package com.exam.orderRequest;



import java.util.List;

public interface OrderRequestService {

    void placeOrder(Long goodsId, Long quantity);

    List<OrderRequestDTO> getAllOrders();

    OrderRequestDTO findTop1ByGoodsOrderByScheduledTimeDesc(Long goodsId);

    void confirmOrder(Long orderId);
}
