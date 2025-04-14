package com.exam.payments;

import java.util.Arrays;

public enum PaymentStatus {
    CANCELED(0),   // 결제 취소
    COMPLETED(1);   // 결제 완료

    private final int code;

    PaymentStatus(int code) {
        this.code = code;
    }

    public static PaymentStatus fromCode(int code) {
        return Arrays.stream(PaymentStatus.values())
                .filter(status -> status.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 결제 상태 코드: " + code));
    }
}
