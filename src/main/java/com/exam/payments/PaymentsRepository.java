package com.exam.payments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments, Long> {

    // 결제번호로 결제 정보 조회
    Optional<Payments> findByPaymentId(Long paymentId);

    // 주문번호로 결제 정보 조회
    Optional<Payments> findByOrdersId(Long ordersId);

}
