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
  public DailyCompositeKey dailyCompositeKey;

  @Column(name = "daily_price")
  Long dailyPrice; // 매출액
  @Column(name = "daily_amount")
  Long dailyAmount; // 주문량

}

