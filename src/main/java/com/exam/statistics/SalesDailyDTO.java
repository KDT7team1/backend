package com.exam.statistics;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SalesDailyDTO {

    LocalDate salesDate;
    int salesHour;
    Long categoryId;  // 대분류 id
    Long subCategoryId; // 소분류 id
    Long dailyPrice;  // 매출액
    Long dailyAmount; // 주문량

}