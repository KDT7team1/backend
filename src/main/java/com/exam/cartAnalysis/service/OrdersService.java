package com.exam.cartAnalysis.service;

import com.exam.cartAnalysis.dto.OrdersDTO;
import com.exam.payments.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface OrdersService {

    OrdersDTO findById(Long orderId);
    void updatePaymentStatus(Long orderId, PaymentStatus status);
    Long createOrder(OrdersDTO dto);
    Page<OrdersDTO> getOrdersListByDate(LocalDate date, PaymentStatus paymentStatus, Pageable pageable);

}
