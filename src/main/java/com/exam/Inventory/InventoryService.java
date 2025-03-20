package com.exam.Inventory;



import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface InventoryService {


    // 1. 재고 전체 테이블 조회
    List<InventoryDTO> findAll();

    // 2. 특정 상품의 재고 정보를 조회
    @Transactional
    InventoryDTO getInventory(Long batchId);

    // 3. 재고 수량 업데이트
//    @Transactional
//    void updateInventory(Long goodsId);

    // 상품 재고 수정 (배치별)
    @Transactional
    void updateStockByBatchId(Long batchId, Long newStock);

    // 상품 재고 수정 (상품테이블)
    void updateGoodsStock(Long goodsId);

    // 상품 재고 감소
    void reduceStock(Long goodsId, Long reduceStock);

    // 상품 재고 증가
    void addStock(Long goodsId, Long addStock, LocalDateTime expirationDate);

}
