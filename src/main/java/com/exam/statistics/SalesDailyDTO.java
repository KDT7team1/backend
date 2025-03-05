package com.exam.statistics;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SalesDailyDTO {

    @EmbeddedId
    com.exam.statistics.CompositeKey compositeKey;

    Long totalAmount; // 매출액
    Long totalOrders; // 주문량
}


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
class CompositeKeyDTO {
    LocalDateTime salesDate;
    Long salesCategory;
}