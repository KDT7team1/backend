package com.exam.statistics;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

  Long totalAmount; // 매출액
  Long totalOrders; // 주문량
}

@Embeddable
class CompositeKey implements java.io.Serializable {
  LocalDateTime salesDate;
  Long salesCategory;
}