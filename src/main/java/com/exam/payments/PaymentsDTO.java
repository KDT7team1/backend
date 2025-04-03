package com.exam.payments;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PaymentsDTO {

    private Long paymentId;
    private Long ordersId;
    private Long memberNo;
    private Long finalPrice;
    private Long paymentAmount;
    private String paymentMethod;
    private LocalDateTime paymentDate;
    private LocalDateTime paymentApproved;

}
