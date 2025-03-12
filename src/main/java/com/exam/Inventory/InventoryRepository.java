package com.exam.Inventory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // 상품 ID로 재고 정보를 조회
    Optional<Inventory> findByGoodsId(Long goodsId);

    // 재고가 부족한 상품 목록 조회
    List<Inventory> findAll();

}
