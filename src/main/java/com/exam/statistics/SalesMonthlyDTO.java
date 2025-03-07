package com.exam.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SalesMonthlyDTO {

    @Id
    @Column(name = "sale_month")
    String saleMonth;

    @Column(name = "total_sales")
    Long monthlyAmount;

    @Column(name = "total_orders")
    Long monthlyOrders;

}