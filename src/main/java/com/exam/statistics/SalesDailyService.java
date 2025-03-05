package com.exam.statistics;

import java.util.List;

public interface SalesDailyService {

    // 1) 선택한 날짜의 매출 통계
    // 1-1) 일 매출 통계
    List<SalesDailyDTO> findBySalesDate(String date);

    // 1-2) 월 매출 통계
    List<SalesDailyDTO> findMonthlySales(String date);
}
