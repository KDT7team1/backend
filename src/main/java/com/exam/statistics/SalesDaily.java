package com.exam.statistics;

import jakarta.persistence.*;
import lombok.*;

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
  CompositeKey compositeKey;

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
class CompositeKey implements java.io.Serializable {

  @Column(name = "sales_date")
  LocalDateTime salesDate;

  @Column(name = "sales_category")
  Long salesCategory;

  @Override
  public boolean equals(Object o){
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CompositeKey that = (CompositeKey) o;
    return Objects.equals(salesDate, that.salesDate) && Objects.equals(salesCategory, that.salesCategory);
  }

  @Override
  public int hashCode() {
    return Objects.hash(salesDate, salesCategory);
  }

}