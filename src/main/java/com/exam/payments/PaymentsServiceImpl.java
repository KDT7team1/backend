package com.exam.payments;

import com.exam.cartAnalysis.entity.Orders;
import com.exam.cartAnalysis.repository.OrdersRepository;
import com.exam.cartAnalysis.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentsServiceImpl implements PaymentsService {

    private final PaymentsRepository paymentsRepository;
    private final OrdersRepository ordersRepository;
    private final OrdersService ordersService;

    @Override
    @Transactional
    public void savePayment(PaymentsDTO paymentsDTO) {
        // `ordersId`를 이용해서 `Orders` 엔티티 조회
        Orders orders = ordersRepository.findById(paymentsDTO.getOrdersId())
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다: " + paymentsDTO.getOrdersId()));

        // 중복 결제 방지 - 주문 상태가 결제 완료면 예외처리
        if (orders.getPaymentStatus() == PaymentStatus.COMPLETED) {
            throw new IllegalArgumentException("이미 결제 완료된 주문입니다.");
        }

        // DTO -> Entity 변환
        Payments payment = Payments.builder()
                .orders(orders)
                .memberNo(paymentsDTO.getMemberNo())
                .finalPrice(paymentsDTO.getFinalPrice())
                .paymentAmount(paymentsDTO.getPaymentAmount())
                .paymentMethod(paymentsDTO.getPaymentMethod())
                .paymentDate(paymentsDTO.getPaymentDate())
                .paymentApproved(paymentsDTO.getPaymentApproved())
                .build();

        paymentsRepository.save(payment);

        // orders 테이블의 payment_status 업데이트
        orders.setPaymentStatus(PaymentStatus.COMPLETED);
        ordersRepository.save(orders);
    }

    @Override
    public Optional<Payments> getPaymentsById(Long paymentsId) {
        // paymentID로 조회
        return paymentsRepository.findByPaymentId(paymentsId);
    }

}
