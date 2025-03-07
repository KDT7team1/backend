package com.exam.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "sales_monthly")
public class SalesMonthly {

    @Id
    @Column(name = "sales_month")
    String saleMonth;

    @Column(name = "monthly_amount")
    Long monthlyAmount;

    @Column(name = "monthly_orders")
    Long monthlyOrders;

}
