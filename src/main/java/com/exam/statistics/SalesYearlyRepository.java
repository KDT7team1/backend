package com.exam.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesYearlyRepository extends JpaRepository<SalesMonthly, MonthlyCompositeKey> {

    // 연간 카테고리 대분류별 매출 통계
    @Query("""
            SELECT
                s.monthlyCompositeKey.categoryId as categoryId,
                SUM(s.monthlyPrice) as yearlyPrice,
                SUM(s.monthlyAmount) as yearlyAmount
            FROM SalesMonthly s
            WHERE substring(s.monthlyCompositeKey.salesMonth, 1, 4) = :year
            GROUP BY s.monthlyCompositeKey.categoryId
            ORDER BY s.monthlyCompositeKey.categoryId
            """)
    List<Object[]> getCategorySalesByYear(@Param("year") String year);

    // 연간 카테고리 소분류별 매출 통계
    @Query("""
            SELECT
                s.monthlyCompositeKey.categoryId as categoryId,
                s.monthlyCompositeKey.subCategoryId as subCategoryId,
                SUM(s.monthlyPrice) as yearlyPrice,
                SUM(s.monthlyAmount) as yearlyAmount
            FROM SalesMonthly s
            WHERE substring(s.monthlyCompositeKey.salesMonth, 1, 4) = :year
            AND s.monthlyCompositeKey.categoryId = :categoryId
            GROUP BY s.monthlyCompositeKey.categoryId, s.monthlyCompositeKey.subCategoryId
            ORDER BY s.monthlyCompositeKey.subCategoryId
            """)
    List<Object[]> getSubCategorySalesByYear(@Param("year") String year, @Param("categoryId") Long categoryId);
}
