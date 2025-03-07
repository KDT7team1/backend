package com.exam.statistics;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SalesDailyDTO {

    LocalDateTime salesDate;
    Long salesCategory;
    Long totalAmount; // 매출액
    Long totalOrders; // 주문량
}