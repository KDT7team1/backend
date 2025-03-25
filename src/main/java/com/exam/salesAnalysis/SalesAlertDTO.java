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
    LocalDate date;

    @NotNull
    Long previous;

    @NotNull
    Long current;

    @NotNull
    Long difference;

    @NotNull
    String message;

    String userComment;
    
}
