package com.exam.salesAnalysis;

import com.exam.salesAlert.SalesAlertDTO;
import com.exam.statistics.SalesDailyDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface SalesAnalysisService {

    // 특정 날짜와 시간대의 매출 데이터 조회
    List<SalesDailyDTO> getSalesDailyByDateAndHour(LocalDate salesDate, int salesHour);

    // 지난 7일간의 시간대 평균 매출액
    long getAverageSalesLast7Days(LocalDate salesDate, int salesHour);

    // 지난 30일간의 시간대 평균 매출액
    long getAverageSalesLast30Days(LocalDate salesDate, int salesHour);

    // 오늘 매출 데이터와 비교하여 이상 감지
    void detectSalesAnomaly(LocalDate salesDate, int salesHour);

}
