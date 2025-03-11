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

    @Column(name = "monthly_amount")
    Long monthlyAmount;

    @Column(name = "monthly_orders")
    Long monthlyOrders;

}

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Embeddable
class MonthlyCompositeKey implements Serializable {

    @Column(name = "sales_month")
    String saleMonth;

    @Column(name = "sales_category")
    Long salesCategory;

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthlyCompositeKey that = (MonthlyCompositeKey) o;
        return Objects.equals(saleMonth, that.saleMonth) && Objects.equals(salesCategory, that.salesCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(saleMonth, salesCategory);
    }

}