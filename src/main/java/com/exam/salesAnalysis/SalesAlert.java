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
@Entity
@Table(name="sales_alert")
public class SalesAlert {

    @Id
    @Column(name="alert_id")
    Long alertId;

    @Column(name="alert_date")
    LocalDate date;

    @Column(name="previous_sales")
    Long previous;

    @Column(name="current_sales")
    Long current;

    @Column(name="difference")
    Long difference;

    @Column(name="alert_message")
    String message;

}
