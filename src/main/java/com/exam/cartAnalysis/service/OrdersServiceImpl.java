package com.exam.cartAnalysis.service;

import com.exam.cartAnalysis.dto.OrdersDTO;
import com.exam.cartAnalysis.entity.Orders;
import com.exam.cartAnalysis.repository.OrdersRepository;
import com.exam.goods.Goods;
import com.exam.goods.GoodsRepository;
import com.exam.payments.PaymentStatus;
import com.exam.saleData.SaleData;
import com.exam.saleData.SaleDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;
    private final GoodsRepository goodsRepository;
    private final SaleDataRepository saleDataRepository;

    @Override
    public OrdersDTO findById(Long orderId) {
        Orders entity = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다: " + orderId));


        // 주문 상세 조회
        List<SaleData> saleData = saleDataRepository.findByOrdersId(orderId);


        // 2. DTO로 변환해서 만들어주기
        List<OrdersDTO.OrderItemDTO> itemDTOs = saleData.stream()
                .map(saleOrder -> {
                    OrdersDTO.OrderItemDTO dto = new OrdersDTO.OrderItemDTO();
                    dto.setGoodsId(saleOrder.getGoods().getGoods_id());
                    dto.setSaleAmount(saleOrder.getSaleAmount().intValue());
                    dto.setSalePrice(saleOrder.getSalePrice());
                    return dto;
                })
                .collect(Collectors.toList());

        return OrdersDTO.builder()
                .ordersId(entity.getOrdersId())
                .memberNo(entity.getMemberNo())
                .ordersDate(entity.getOrdersDate())
                .finalPrice(entity.getFinalPrice())
                .orderSummary(entity.getOrderSummary())
                .paymentStatus(entity.getPaymentStatus())
                .orderItems(itemDTOs)
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

        // 2. orderItems를 기반으로 sale_data 저장
        for (OrdersDTO.OrderItemDTO item : dto.getOrderItems()) {
            Goods goods = goodsRepository.findById(item.getGoodsId())
                    .orElseThrow(() -> new IllegalArgumentException("상품 없음"));

            SaleData sale = SaleData.builder()
                    .orders(order)
                    .goods(goods)
                    .saleAmount(item.getSaleAmount().longValue())
                    .salePrice(item.getSalePrice())
                    .saleDate(LocalDateTime.now())
                    .build();

            saleDataRepository.save(sale);
        }

        return savedOrder.getOrdersId();
    }

}
