package com.exam.statistics;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
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

  @Column(name = "total_amount")
  Long totalAmount; // 매출액
  @Column(name = "total_orders")
  Long totalOrders; // 주문량
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
  LocalDateTime salesDate;

  @Column(name = "sales_category")
  Long salesCategory;

  @Override
  public boolean equals(Object o){
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DailyCompositeKey that = (DailyCompositeKey) o;
    return Objects.equals(salesDate, that.salesDate) && Objects.equals(salesCategory, that.salesCategory);
  }

  @Override
  public int hashCode() {
    return Objects.hash(salesDate, salesCategory);
  }

}