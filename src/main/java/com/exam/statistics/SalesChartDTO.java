package com.exam.statistics;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SalesChartDTO {

    private LocalDate date;
    private int totalSales;

}