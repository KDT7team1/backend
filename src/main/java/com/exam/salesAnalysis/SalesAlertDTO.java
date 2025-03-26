package com.exam.salesAnalysis;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SalesAlertDTO {

    Long alertId;

    @NotNull
    int trendBasis;

    @NotNull
    LocalDate alertDate;

    @NotNull
    Long previousSales;

    @NotNull
    Long currentSales;

    @NotNull
    Long difference;

    @NotNull
    String alertMessage;

    String userComment;
    
}
