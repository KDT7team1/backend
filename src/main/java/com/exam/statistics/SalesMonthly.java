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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Embeddable
class MonthlyCompositeKey implements Serializable {

    @Column(name = "sales_month")
    String salesMonth;

    @Column(name = "category_id")
    Long categoryId;

    @Column(name = "sub_category_id")
    Long subCategoryId;

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthlyCompositeKey that = (MonthlyCompositeKey) o;
        return Objects.equals(salesMonth, that.salesMonth) && Objects.equals(categoryId, that.categoryId) && Objects.equals(subCategoryId, that.subCategoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(salesMonth, categoryId, subCategoryId);
    }

}