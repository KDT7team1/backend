package com.exam.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SalesDailyRepository extends JpaRepository<SalesDaily, DailyCompositeKey> {

    // 선택한 날짜의 ! 시간별 ! 매출 통계
    @Query("SELECT s FROM SalesDaily s WHERE s.dailyCompositeKey.salesDate BETWEEN :startDay AND :endDay")
    List<SalesDaily> findBySalesDate(@Param("startDay") LocalDateTime startDay,
                                     @Param("endDay") LocalDateTime endDay);

}
