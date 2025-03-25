package com.exam.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesDailyRepository extends JpaRepository<SalesDaily, DailyCompositeKey> {

    // 선택한 날짜의 시간별 매출 통계
    @Query("""
            SELECT
                s.dailyCompositeKey.salesHour as salesHour,
                coalesce(sum(s.dailyPrice), 0) as dailyPrice,
                coalesce(sum(s.dailyAmount), 0) as dailyAmount
            FROM SalesDaily s
            WHERE s.dailyCompositeKey.salesDate = :date
            GROUP BY s.dailyCompositeKey.salesHour
            ORDER BY s.dailyCompositeKey.salesHour
            """)
    List<Object[]> getHourlySalesByDate(@Param("date") LocalDate date);

    // 선택한 월에 해당하는 날짜별 매출 통계
    @Query("""
            SELECT 
                s.dailyCompositeKey.salesDate as salesDate,
                sum(s.dailyPrice) as dailyPrice,
                sum(s.dailyAmount) as dailyAmount
            FROM SalesDaily s
            WHERE FUNCTION('DATE_FORMAT', s.dailyCompositeKey.salesDate, '%Y-%m') = :month
            GROUP BY s.dailyCompositeKey.salesDate
            ORDER BY s.dailyCompositeKey.salesDate
            """)
    List<Object[]> getDailySalesByMonth(@Param("month") String month);

    // 선택한 날짜의 카테고리별 매출통계 - 대분류 조회
    @Query("""
            SELECT
                s.dailyCompositeKey.categoryId as categoryId,
                SUM(s.dailyPrice) as totalPrice,
                SUM(s.dailyAmount) as totalAmount
            FROM SalesDaily s
            WHERE s.dailyCompositeKey.salesDate = :date
            GROUP BY s.dailyCompositeKey.categoryId
            ORDER BY s.dailyCompositeKey.categoryId
            """)
    List<Object[]> getCategorySalesByDate(@Param("date") LocalDate date);

    // 선택한 날짜의 카테고리별 매출통계 - 소분류 조회
    @Query("""
            SELECT 
                s.dailyCompositeKey.categoryId as categoryId,
                s.dailyCompositeKey.subCategoryId as subCategoryId,
                SUM(s.dailyPrice) as totalPrice,
                SUM(s.dailyAmount) as totalAmount
            FROM SalesDaily s
            WHERE s.dailyCompositeKey.salesDate = :date
            AND s.dailyCompositeKey.categoryId = :categoryId
            GROUP BY s.dailyCompositeKey.categoryId, s.dailyCompositeKey.subCategoryId
            ORDER BY s.dailyCompositeKey.subCategoryId
            """)
    List<Object[]> getSubCategorySalesByDate(@Param("date") LocalDate date, @Param("categoryId") Long categoryId);


    // 선택한 날짜 사이(7, 30일간)의 시간대별 매출 평균
    @Query("""
            SELECT
                s.dailyCompositeKey.salesHour as salesHour,
                COALESCE(AVG(s.dailyPrice)) as averagePrice,
                COALESCE(AVG(s.dailyAmount)) as averageAmount
            FROM SalesDaily s
            WHERE s.dailyCompositeKey.salesDate BETWEEN :startDate AND :endDate
            GROUP BY s.dailyCompositeKey.salesHour
            ORDER BY s.dailyCompositeKey.salesHour
            """)
    Object[] getAvgHourlySalesByDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // 선택한 날짜 사이(7, 30일간)의 카테고리별 매출 평균
    @Query("""
            SELECT
                s.dailyCompositeKey.categoryId as categoryId,
                s.dailyCompositeKey.subCategoryId as subCategoryId,
                COALESCE(AVG(s.dailyPrice)) as averagePrice,
                COALESCE(AVG(s.dailyAmount)) as averageAmount
            FROM SalesDaily s
            WHERE s.dailyCompositeKey.salesDate BETWEEN :startDate AND :endDate
            GROUP BY s.dailyCompositeKey.categoryId, s.dailyCompositeKey.subCategoryId
            ORDER BY s.dailyCompositeKey.categoryId, s.dailyCompositeKey.subCategoryId
            """)
    Object[] getAvgCategorySalesByDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // 선택한 날짜 사이(7, 30일간)의 시간대별 매출량 전체
    @Query("""
            SELECT
                s.dailyCompositeKey.salesDate as salesDate,
                s.dailyCompositeKey.salesHour as salesHour,
                SUM(s.dailyPrice) as averagePrice,
                SUM(s.dailyAmount) as averageAmount
            FROM SalesDaily s
            WHERE s.dailyCompositeKey.salesDate BETWEEN :startDate AND :endDate
            GROUP BY s.dailyCompositeKey.salesHour
            ORDER BY s.dailyCompositeKey.salesHour
            """)
    List<Object[]> getTotalHourlySalesByDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // 선택한 날짜 사이(7, 30일간)의 카테고리별 매출량 전체
    @Query("""
            SELECT
                s.dailyCompositeKey.salesDate as salesDate,
                s.dailyCompositeKey.categoryId as categoryId,
                s.dailyCompositeKey.subCategoryId as subCategoryId,
                SUM(s.dailyPrice) as averagePrice,
                SUM(s.dailyAmount) as averageAmount
            FROM SalesDaily s
            WHERE s.dailyCompositeKey.salesDate BETWEEN :startDate AND :endDate
            GROUP BY s.dailyCompositeKey.categoryId, s.dailyCompositeKey.subCategoryId
            ORDER BY s.dailyCompositeKey.categoryId, s.dailyCompositeKey.subCategoryId
            """)
    List<Object[]> getTotalCategorySalesByDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}


