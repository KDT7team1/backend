package com.exam.statistics;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SalesMonthlyDTO {

    String saleMonth;
    Long salesCategory;
    Long monthlyAmount;
    Long monthlyOrders;

}