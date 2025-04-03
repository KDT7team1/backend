package com.exam.cartAnalysis.service;

import com.exam.cartAnalysis.dto.OrdersDTO;
import com.exam.cartAnalysis.entity.Orders;
import com.exam.cartAnalysis.repository.OrdersRepository;
import com.exam.payments.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;

    @Override
    public OrdersDTO findById(Long orderId) {
        Orders entity = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다: " + orderId));

        return OrdersDTO.builder()
                .ordersId(entity.getOrdersId())
                .memberNo(entity.getMemberNo())
                .ordersDate(entity.getOrdersDate())
                .finalPrice(entity.getFinalPrice())
                .orderSummary(entity.getOrderSummary())
                .paymentStatus(entity.getPaymentStatus())
                .build();
    }

    @Override
    @Transactional
    public void updatePaymentStatus(Long orderId, PaymentStatus status) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다: " + orderId));

        order.setPaymentStatus(status);
        ordersRepository.save(order);
    }

    @Override
    @Transactional
    public Long createOrder(OrdersDTO dto) {
        Orders order = Orders.builder()
                .memberNo(dto.getMemberNo())
                .ordersDate(LocalDateTime.now())
                .finalPrice(dto.getFinalPrice())
                .orderSummary(dto.getOrderSummary())
                .paymentStatus(dto.getPaymentStatus())
                .build();

        Orders savedOrder = ordersRepository.save(order);
        return savedOrder.getOrdersId();
    }

}
