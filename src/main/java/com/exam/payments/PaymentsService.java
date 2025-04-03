package com.exam.payments;

import java.util.Optional;

public interface PaymentsService {

    // 결제 정보 저장
    void savePayment(PaymentsDTO paymentsDTO);

    // 결제 정보 조회
    Optional<Payments> getPaymentsById(Long paymentsId);

}
