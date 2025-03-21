package com.exam.statistics;

import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SalesDailyService {

    // 1) 선택한 날짜의 시간/카테고리별 매출 통계
    List<SalesDailyDTO> findBySalesDate(LocalDate searchDate);

    // 2) 선택한 날짜의 시간별 매출 통계
    List<SalesDailyDTO> getHourlySalesByDate(LocalDate date);

    // 3) 선택한 월의 날짜별 매출 통계
    List<SalesDailyDTO> getDailySalesByMonth(String month);
}
