package com.exam.salesAnalysis;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    LocalDate date;
    Long previous;
    Long current;
    Long difference;
    String message;
    
}
