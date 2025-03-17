package com.exam.Inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InventoryController {

    @Autowired
    InventoryServiceImpl inventoryService;

    // 특정 상품의 재고 정보를 조회
    @GetMapping("/{goodsId}")
    public InventoryDTO getInventory(@PathVariable Long goodsId){
        return inventoryService.getInventory(goodsId);
    }

    // 재고 수량 업데이트
    @PostMapping("/update/{goodsId}")
    public String updateInventory(@PathVariable Long goodsId, @RequestParam Long stockQuantity){
        inventoryService.updateInventory(goodsId, stockQuantity);

        return "재고 수량이 업데이트 되었습니다.";
    }

    // 재고가 부족한 상품 목록 조회
    @GetMapping("/low-stock")
    public List<InventoryDTO> getLowStockItems(){
        return inventoryService.getLowStockItems();
    }

}
