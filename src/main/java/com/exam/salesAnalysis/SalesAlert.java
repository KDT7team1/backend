package com.exam.salesAnalysis;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="alert_id")
    Long alertId;

    @Column(name="trend_basis")
    int trendBasis; // 7: 요일 기반, 30: 월별 계절 트렌드 기반

    @Column(name="alert_date")
    LocalDate alertDate;

    @Column(name="previous_sales")
    Long previousSales;

    @Column(name="current_sales")
    Long currentSales;

    @Column(name="difference")
    Long difference;

    @Column(name="alert_message")
    String alertMessage;

    @Column(name="user_comment")
    String userComment;

}
