package com.exam.statistics;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "sales_monthly")
public class SalesMonthly {

    @EmbeddedId
    MonthlyCompositeKey monthlyCompositeKey;

    @Column(name = "monthly_price")
    Long monthlyPrice;

    @Column(name = "monthly_amount")
    Long monthlyAmount;

}