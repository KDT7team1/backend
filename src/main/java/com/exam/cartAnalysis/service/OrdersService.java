package com.exam.cartAnalysis.service;

import com.exam.cartAnalysis.dto.OrdersDTO;
import com.exam.cartAnalysis.entity.Orders;
import com.exam.payments.PaymentStatus;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;

public interface OrdersService {

    OrdersDTO findById(Long orderId);
    void updatePaymentStatus(Long orderId, PaymentStatus status);
    Long createOrder(OrdersDTO dto);
    Page<OrdersDTO> getOrdersListByDate(LocalDate date, Pageable pageable);

}
