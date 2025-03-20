package com.exam.Inventory;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // 상품 ID로 재고 정보를 조회
    @Query("select i from Inventory i where i.goods.goods_id = :goodsId")
    List<Inventory> findByGoodsId(@Param("goodsId") Long goodsId);

    @Query("select i from Inventory i order by i.goods.goods_id ASC, i.expirationDate ASC")
    List<Inventory> findAll();
}
