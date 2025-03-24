package com.exam.statistics;

import java.util.List;

public interface SalesYearlyService {

    // 연간 카테고리 대분류별 매출 통계
    List<SalesYearlyDTO> getCategorySalesByYear(String year);

    // 연간 카테고리 소분류별 매출 통계
    List<SalesYearlyDTO> getSubCategorySalesByYear(String year, Long categoryId);

}
