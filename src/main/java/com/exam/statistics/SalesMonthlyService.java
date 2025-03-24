package com.exam.statistics;

import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SalesMonthlyService {
    // 선택한 월의 매출 통계
    List<SalesMonthlyDTO> findBySalesMonth(String salesMonth);

    // 선택한 연도의 전체 매출 통계
    List<SalesMonthlyDTO> findBySalesYear(String year);

    // 선택한 연도의 월간 매출 통계
    List<SalesMonthlyDTO> getMonthlySalesByYear(String year);

    // 월간 카테고리 대분류별 매출 통계
    List<SalesMonthlyDTO> getCategorySalesByMonth(String month);

    // 월간 카테고리 소분류별 매출 통계
    List<SalesMonthlyDTO> getSubCategorySalesByMonth(String month, Long categoryId);
}
