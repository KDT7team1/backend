package com.exam.saleData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
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


    // ordersId 로 판매기록 찾기
    @Query("select s from SaleData s where s.orders.ordersId = :ordersId")
    List<SaleData> findByOrdersId( @Param("ordersId") Long ordersId);

    // ordersId로 sale_data 지우기
    @Transactional
    void deleteByOrders_OrdersId(Long ordersId); // Orders 객체 기준 FK 매핑된 필드 이름 사용

    // ordersId로 상세 판매기록 찾기 - 영수증 조회용
    @Query("""
            SELECT s FROM SaleData s
            JOIN FETCH s.goods g
            JOIN FETCH s.orders o
            WHERE o.ordersId = :ordersId
            """)
    List<SaleData> findByOrdersIdWithDetails(@Param("ordersId") Long ordersId);

    // 매 시 정각에 통계 테이블 업데이트하기
    @Query(value = """
            SELECT
                DATE(s.sale_date) as salesDate,
                HOUR(s.sale_date) as salesHour,
                g.category_id as categoryId,
                g.sub_category_id as subCategoryId,
                SUM(s.sale_price) as totalPrice,
                SUM(s.sale_amount) as totalAmount
            FROM sale_data s
            JOIN goods g ON s.goods_id = g.goods_id
            WHERE s.sale_date >= :start AND s.sale_date < :end
            GROUP BY salesDate, salesHour, categoryId, subCategoryId
            """, nativeQuery = true)
    List<Object[]> findHourlyAggregatedSales(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // 매 월 1일에 통계 테이블 업데이트
    @Query(value = """
    SELECT 
        DATE_FORMAT(s.sale_date, '%Y-%m') AS salesMonth,
        g.category_id AS categoryId,
        g.sub_category_id AS subCategoryId,
        SUM(s.sale_price) AS totalPrice,
        SUM(s.sale_amount) AS totalAmount
    FROM sale_data s
    JOIN goods g ON s.goods_id = g.goods_id
    WHERE s.sale_date >= :start AND s.sale_date < :end
    GROUP BY salesMonth, categoryId, subCategoryId
""", nativeQuery = true)
    List<Object[]> findMonthlyAggregatedSales(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}

