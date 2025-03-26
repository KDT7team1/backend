package com.exam.Inventory;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // 상품 ID로 재고 정보를 조회
    @Query("select i from Inventory i where i.goods.goods_id = :goodsId")
    List<Inventory> findByGoodsId(@Param("goodsId") Long goodsId);

    @Query("select i from Inventory i order by i.goods.goods_id ASC, i.expirationDate ASC")
    List<Inventory> findAll();

    @Query("""
    SELECT i FROM Inventory i
    WHERE i.expirationDate <= CURRENT_DATE
      AND i.stockQuantity > 0
      AND NOT EXISTS (
          SELECT 1 FROM Disposal d WHERE d.inventory.batchId = i.batchId
      )
    ORDER BY i.expirationDate ASC
    """)
    List<Inventory> findExpiredButNotDisposed();


    @Query("""
    select i from Inventory i 
    where i.expirationDate BETWEEN :now AND :limit
    and i.stockQuantity > 0 
    order by i.expirationDate ASC  
    """)
    List<Inventory> findExpiringSoonItems(@Param("now") LocalDateTime now, @Param("limit") LocalDateTime limit);

}
