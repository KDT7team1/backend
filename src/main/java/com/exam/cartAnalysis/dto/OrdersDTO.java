package com.exam.cartAnalysis.dto;
import com.exam.member.Member;
import com.exam.payments.PaymentStatus;
import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class OrdersDTO {

    Long ordersId;
    Long memberNo;
    LocalDateTime ordersDate;
    Long finalPrice;
    String orderSummary;
    PaymentStatus paymentStatus;

}
