package com.exam.statistics;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SalesMonthlyDTO {

    String salesMonth;
    Long categoryId;
    Long subCategoryId;
    Long monthlyPrice;
    Long monthlyAmount;

}