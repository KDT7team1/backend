package com.exam.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesMonthlyRepository extends JpaRepository<SalesMonthly, String> {
    // 선택한 월의 매출 통계
    @Query("SELECT s FROM SalesMonthly s WHERE s.saleMonth = :salesMonth")
    List<SalesMonthly> findBySalesMonth(String salesMonth);
}
