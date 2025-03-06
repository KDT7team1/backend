package com.exam.statistics;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
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
public class SalesDailyDTO {

    @EmbeddedId
    CompositeKeyDTO compositeKeyDTO;

    Long totalAmount; // 매출액
    Long totalOrders; // 주문량
}

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Embeddable
class CompositeKeyDTO implements Serializable {
    LocalDateTime salesDate;
    Long salesCategory;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeKeyDTO that = (CompositeKeyDTO) o;
        return Objects.equals(salesDate, that.salesDate) && Objects.equals(salesCategory, that.salesCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(salesDate, salesCategory);
    }
}