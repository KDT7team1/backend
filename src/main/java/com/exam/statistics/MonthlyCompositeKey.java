package com.exam.statistics;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Embeddable
public class MonthlyCompositeKey implements Serializable {

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
