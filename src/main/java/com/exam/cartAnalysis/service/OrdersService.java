package com.exam.cartAnalysis.service;

import com.exam.cartAnalysis.dto.OrdersDTO;
import com.exam.payments.PaymentStatus;

public interface OrdersService {

    OrdersDTO findById(Long orderId);
    void updatePaymentStatus(Long orderId, PaymentStatus status);
    Long createOrder(OrdersDTO dto);

}
