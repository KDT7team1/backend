package com.exam.Inventory;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // 상품 ID로 재고 정보를 조회
    @Query("select i from Inventory i where i.goods.goods_id = :goodsId")
    Optional<Inventory> findByGoodsId(@Param("goodsId") Long goodsId);

}
