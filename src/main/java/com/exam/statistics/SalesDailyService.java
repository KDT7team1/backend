package com.exam.statistics;

import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SalesDailyService {

    // 1) 선택한 날짜의 시간별 매출 통계
    List<SalesDailyDTO> getHourlySalesByDate(LocalDate date);

    // 2) 선택한 월의 날짜별 매출 통계
    List<SalesDailyDTO> getDailySalesByMonth(String month);

    // 3) 선택한 날짜의 카테고리별 매출 통계 - 대분류
    List<SalesDailyDTO> getCategorySalesByDate(LocalDate date);

    // 4) 선택한 날짜의 카테고리별 매출 통계 - 소분류
    List<SalesDailyDTO> getSubCategorySalesByDate(LocalDate date, Long categoryId);

    // 5) 선택한 날짜로부터 n일간의 시간별 매출 평균
    SalesDailyDTO getAvgHourlySalesByDate(LocalDate startDate, LocalDate endDate);

    // 6) 선택한 날짜로부터 n일간의 카테고리별 매출 평균
    SalesDailyDTO getAvgCategorySalesByDate(LocalDate startDate, LocalDate endDate);

}
