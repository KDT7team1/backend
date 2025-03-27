package com.exam.salesAlert;

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
    // 1: 저번 주 같은 요일 대비 | 7: 7일간 평균 | 30: 30일간 평균 대비
    int trendBasis;

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
