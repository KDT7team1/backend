package com.exam.disposal;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DisposalRepository extends JpaRepository<Disposal, Long> {

    // 날짜별로 폐기 테이블 확인하기
    @Query("SELECT d FROM Disposal d where Date(d.disposed_at) = :selectedDate")
    List<Disposal> findByDisposedAtDate(@Param("selectedDate") LocalDate selectedDate);

    // 월별로 폐기 조회하기 (카테고리별)
    @Query("""
    select new com.exam.disposal.DisposalStatsDTO(s.sub_name,SUM(d.disposed_quantity))
    from Disposal d
    join d.goods g
    join g.subCategory s
    where function('MONTH', d.disposed_at) = :month
      and function('YEAR', d.disposed_at) = :year 
    group by s.sub_name
    """)
    List<DisposalStatsDTO> findMonthlyDisposal(@Param("month") int month, @Param("year") int year);


    // 재고수량
    @Query("""
SELECT new com.exam.disposal.DisposalRateDTO(
    sc.sub_name,
    COALESCE(SUM(inv.totalStock), 0),
    COALESCE(SUM(sd.totalSales), 0),
    COALESCE(SUM(dp.totalDisposed), 0),
    CASE
        WHEN COALESCE(SUM(inv.totalStock), 0) = 0 THEN 0.0
        ELSE ROUND(SUM(dp.totalDisposed) * 1.0 / SUM(inv.totalStock) * 100, 2)
    END
)
FROM SubCategory sc
JOIN Goods g ON g.subCategory.sub_category_id = sc.sub_category_id
LEFT JOIN (
    SELECT i.goods.goods_id AS goodsId, SUM(i.initialStockQuantity) AS totalStock
    FROM Inventory i
    WHERE FUNCTION('MONTH', i.stockCreatedAt) = :month AND FUNCTION('YEAR', i.stockCreatedAt) = :year
    GROUP BY i.goods.goods_id
) AS inv ON g.goods_id = inv.goodsId
LEFT JOIN (
    SELECT s.goods.goods_id AS goodsId, SUM(s.saleAmount) AS totalSales
    FROM SaleData s
    WHERE FUNCTION('MONTH', s.saleDate) = :month AND FUNCTION('YEAR', s.saleDate) = :year
    GROUP BY s.goods.goods_id
) AS sd ON g.goods_id = sd.goodsId
LEFT JOIN (
    SELECT d.goods.goods_id AS goodsId, SUM(d.disposed_quantity) AS totalDisposed
    FROM Disposal d
    WHERE FUNCTION('MONTH', d.disposed_at) = :month AND FUNCTION('YEAR', d.disposed_at) = :year
    GROUP BY d.goods.goods_id
) AS dp ON g.goods_id = dp.goodsId
WHERE sc.sub_name IN :subNames
GROUP BY sc.sub_name
""")
    List<DisposalRateDTO> getDisposalRate(
            @Param("subNames") List<String> subNames,
            @Param("month") int month,
            @Param("year") int year
    );




}

