package com.exam.Inventory;

import java.util.List;

public interface InventoryService {

    // 특정 상품의 재고 정보를 조회
    InventoryDTO getInventory(Long goodsId);

    // 재고 수량 업데이트
    void updateInventory(Long goodsId, Long stockQuantity);

    // 재고가 부족한 상품 목록 조회
    List<InventoryDTO> getLowStockItems();

}
