package com.exam.orderRequest;


import java.util.List;

public interface OrderRequestService {

    void placeOrder(Long goodsId, Long quantity);

    List<OrderDTO> getAllOrders();

    void completeOrder(Long orderId);


}
