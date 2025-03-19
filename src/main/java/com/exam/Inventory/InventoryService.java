package com.exam.Inventory;



import java.util.List;

public interface InventoryService {


    // 1. 재고 전체 테이블 조회
    List<InventoryDTO> findAll();

    // 2. 특정 상품의 재고 정보를 조회
    InventoryDTO getInventory(Long goodsId);

    // 3. 재고 수량 업데이트
    void updateInventory(Long goodsId);



}
