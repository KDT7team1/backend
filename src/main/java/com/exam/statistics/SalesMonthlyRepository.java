package com.exam.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
