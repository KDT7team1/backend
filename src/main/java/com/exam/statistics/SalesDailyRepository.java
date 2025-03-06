package com.exam.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SalesDailyRepository extends JpaRepository<SalesDaily, CompositeKey> {

    // 선택한 날짜의 매출 통계
    @Query("SELECT s FROM SalesDaily s WHERE s.compositeKey.salesDate = :salesDate")
    List<SalesDaily> findBySalesDate(LocalDateTime salesDate);
}
