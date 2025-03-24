package com.exam.statistics;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "sales_daily")
public class SalesDaily {

  @EmbeddedId
  DailyCompositeKey dailyCompositeKey;

  @Column(name = "daily_price")
  Long dailyPrice; // 매출액
  @Column(name = "daily_amount")
  Long dailyAmount; // 주문량
}

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Embeddable
class DailyCompositeKey implements Serializable {

  @Column(name = "sales_date")
  LocalDate salesDate;

  @Column(name = "sales_hour")
  int salesHour;

  @Column(name = "category_id")
  Long categoryId;

  @Column(name = "sub_category_id")
  Long subCategoryId;

  @Override
  public boolean equals(Object o){
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DailyCompositeKey that = (DailyCompositeKey) o;
    return salesHour == that.salesHour && Objects.equals(salesDate, that.salesDate) && Objects.equals(categoryId, that.categoryId) && Objects.equals(subCategoryId, that.subCategoryId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(salesDate, categoryId, subCategoryId, salesHour);
  }

}