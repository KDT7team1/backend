package com.exam.saleData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleDataRepository extends JpaRepository<SaleData, Long>{

    @Query("SELECT s FROM SaleData s WHERE s.saleDate BETWEEN :startTime AND :endTime")
    List<SaleData> findSalesByDateTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT count(s) FROM SaleData s WHERE s.saleDate BETWEEN :startTime AND :endTime")
    Long getTodayVisitors(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT sum(s.salePrice) FROM SaleData s WHERE s.saleDate BETWEEN :startTime AND :endTime")
    Long getTodaySales(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    // 최근 7일 판매량
    @Query("SELECT DATE(s.saleDate), SUM(s.saleAmount) " +
            "FROM SaleData s " +
            "WHERE s.goods.goods_id = :goodsId " +
            "AND s.saleDate BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(s.saleDate) " +
            "ORDER BY DATE(s.saleDate)")
    List<Object[]> findWeeklySalesByGoodsId(
            @Param("goodsId") Long goodsId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


    // ordersIs d로 판매기록 찾기
    @Query("select s from SaleData s where s.orders.ordersId = :ordersId")
    List<SaleData> findByOrdersId( @Param("ordersId") Long ordersId);

    // ordersId로 sale_data 지우기
    @Transactional
    void deleteByOrders_OrdersId(Long ordersId); // Orders 객체 기준 FK 매핑된 필드 이름 사용


}

