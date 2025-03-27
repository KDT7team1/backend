package com.exam.salesAnalysis;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SalesDataRepository extends JpaRepository<SalesData, Long> {

    List<SalesData> findBySalesDateBetween(LocalDateTime startDate, LocalDateTime endDate);

}
