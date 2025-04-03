package com.exam.cartAnalysis.service;

import com.exam.cartAnalysis.dto.OrdersDTO;
import com.exam.cartAnalysis.entity.Orders;
import com.exam.cartAnalysis.repository.OrdersRepository;
import com.exam.payments.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .member(entity.getMember())
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

}
