package com.exam.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesDailyRepository extends JpaRepository<SalesDaily, DailyCompositeKey> {

    // 선택한 날짜의 시간별 + 카테고리별 매출 통계
    @Query("SELECT s FROM SalesDaily s WHERE s.dailyCompositeKey.salesDate = :date")
    List<SalesDaily> findBySalesDate(@Param("date") LocalDate date);

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
}
