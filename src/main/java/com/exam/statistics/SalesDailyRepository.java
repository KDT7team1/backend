package com.exam.statistics;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalesDailyRepository extends JpaRepository<SalesDaily, CompositeKey> {

    // 1) 선택한 날짜의 매출 통계
    // 1-1) 일 매출 통계
    List<SalesDaily> findBySalesDate(String date);

    // 1-2) 월 매출 통계
    List<SalesDaily> findMonthlySales(String date);
}
