package com.exam.statistics;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SalesDailyDTO {

    LocalDate salesDate;
    int salesHour;
    Long salesCategory;
    Long totalAmount; // 매출액
    Long totalOrders; // 주문량
}