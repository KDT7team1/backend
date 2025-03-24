package com.exam.statistics;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SalesYearlyDTO {

    String year;
    Long categoryId;
    Long subCategoryId;
    Long yearlyPrice;
    Long yearlyAmount;

}
