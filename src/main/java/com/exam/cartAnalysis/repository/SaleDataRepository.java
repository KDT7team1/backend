package com.exam.cartAnalysis.repository;

import com.exam.cartAnalysis.entity.SaleData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleDataRepository extends JpaRepository<SaleData, Long>{

    @Query("SELECT s FROM SaleData s WHERE s.saleDate BETWEEN :startTime AND :endTime")
    List<SaleData> findSalesByDateTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

}

