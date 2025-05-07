package com.exam.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesMonthlyRepository extends JpaRepository<SalesMonthly, MonthlyCompositeKey> {

    // 선택한 월의 매출 통계
    @Query("SELECT s FROM SalesMonthly s WHERE s.monthlyCompositeKey.salesMonth = :salesMonth")
    List<SalesMonthly> findBySalesMonth(String salesMonth);

    // 선택한 연도의 전체 매출 통계
    @Query("SELECT s FROM SalesMonthly s WHERE s.monthlyCompositeKey.salesMonth like :year")
    List<SalesMonthly> findBySalesYear(String year);

    // 선택한 연도의 월간 매출 통계
    @Query("""
            SELECT
                s.monthlyCompositeKey.salesMonth as salesMonth,
                sum(s.monthlyPrice) as monthlyPrice,
                sum(s.monthlyAmount) as monthlyAmount
            FROM SalesMonthly s
            WHERE substring(s.monthlyCompositeKey.salesMonth, 1, 4) = :year
            GROUP BY s.monthlyCompositeKey.salesMonth
            ORDER BY s.monthlyCompositeKey.salesMonth
            """)
    List<Object[]> getMonthlySalesByYear(@Param("year") String year);

    // 월간 카테고리 대분류별 매출 통계
    @Query("""
            SELECT
                s.monthlyCompositeKey.categoryId as categoryId,
                SUM(s.monthlyPrice) as monthlyPrice,
                SUM(s.monthlyAmount) as monthlyAmount
            FROM SalesMonthly s
            WHERE s.monthlyCompositeKey.salesMonth = :month
            GROUP BY s.monthlyCompositeKey.categoryId
            ORDER BY s.monthlyCompositeKey.categoryId
            """)
    List<Object[]> getCategorySalesByMonth(@Param("month") String month);

    // 월간 카테고리 소분류별 매출 통계
    @Query("""
            SELECT
                s.monthlyCompositeKey.categoryId as categoryId,
                s.monthlyCompositeKey.subCategoryId as subCategoryId,
                SUM(s.monthlyPrice) as monthlyPrice,
                SUM(s.monthlyAmount) as monthlyAmount
            FROM SalesMonthly s
            WHERE s.monthlyCompositeKey.salesMonth = :month
            AND s.monthlyCompositeKey.categoryId = :categoryId
            GROUP BY s.monthlyCompositeKey.categoryId, s.monthlyCompositeKey.subCategoryId
            ORDER BY s.monthlyCompositeKey.subCategoryId
            """)
    List<Object[]> getSubCategorySalesByMonth(@Param("month") String month, @Param("categoryId") Long categoryId);


    // 스케줄링 - 월간 데이터 업데이트
    @Modifying
    @Query(value = """
        UPDATE sales_monthly
        SET monthly_price = :price,
            monthly_amount = :amount
        WHERE sales_month = :month
          AND category_id = :cat
          AND sub_category_id = :subCat
        """, nativeQuery = true)
    int updateMonthlyStat(@Param("month") String month,
                          @Param("cat") Long categoryId,
                          @Param("subCat") Long subCategoryId,
                          @Param("price") Long price,
                          @Param("amount") Long amount);

    @Modifying
    @Query(value = """
        INSERT INTO sales_monthly
        (sales_month, category_id, sub_category_id, monthly_price, monthly_amount)
        VALUES (:month, :cat, :subCat, :price, :amount)
        """, nativeQuery = true)
    void insertMonthlyStat(@Param("month") String month,
                           @Param("cat") Long categoryId,
                           @Param("subCat") Long subCategoryId,
                           @Param("price") Long price,
                           @Param("amount") Long amount);
}
