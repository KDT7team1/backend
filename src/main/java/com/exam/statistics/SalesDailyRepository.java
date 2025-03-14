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
            select
                s.dailyCompositeKey.salesHour as salesHour,
                coalesce(sum(s.dailyPrice), 0) as dailyPrice,
                coalesce(sum(s.dailyAmount), 0) as dailyAmount
            from SalesDaily s
            where s.dailyCompositeKey.salesDate = :date
            group by s.dailyCompositeKey.salesHour
            order by s.dailyCompositeKey.salesHour
            """)
    List<SalesDaily> getHourlySalesByDate(@Param("date") LocalDate date);
}
