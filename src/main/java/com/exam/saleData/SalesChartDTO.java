package com.exam.saleData;

import com.exam.cartAnalysis.entity.Orders;
import com.exam.goods.Goods;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder

public class SalesChartDTO {
    private LocalDate date;
    private int amount;
}
