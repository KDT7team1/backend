package com.exam.Inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/inventory")
@RestController
public class InventoryController {

    @Autowired
    InventoryServiceImpl inventoryService;

    // 1. 재고테이블 전체 조회
    @GetMapping("/findAll")
    public ResponseEntity<List<InventoryDTO>> findAll(){
        List<InventoryDTO> inventoryList = inventoryService.findAll();
        return ResponseEntity.status(200).body(inventoryList);
    }

    // 2. 특정 상품의 재고 정보를 조회
    @GetMapping("/findById/{batchId}")
    public ResponseEntity<InventoryDTO> getInventory(@PathVariable Long batchId){
        InventoryDTO dto =  inventoryService.getInventory(batchId);

        if (dto == null) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.status(200).body(dto);
    }

    // 3. 재고 수량 업데이트 ( 배치별 )
    @PutMapping("/update/{batchId}/{newStock}")
    public ResponseEntity<String> updateInventory(@PathVariable Long batchId,
                                                  @PathVariable Long newStock){
        System.out.println("재고 업데이트 API 호출됨: batchId = " + batchId);
        inventoryService.updateStockByBatchId(batchId, newStock);

        return ResponseEntity.ok("배치 " + batchId + "의 재고가 " + newStock + "으로 변경되었습니다.");
    }

    // 3. 상품 재고 수정하기
    @PutMapping("/updateStock/{goodsId}")
    public ResponseEntity<String> updateStock(@PathVariable Long goodsId, @RequestParam("newStock") Long newStock) {

        inventoryService.updateGoodsStock(goodsId);

        return ResponseEntity.ok("Stock updated successfully");
    }

    // 상품 감소 로직
    @PostMapping("reduceStock")
    public ResponseEntity<String> reduceStock(
            @RequestParam Long goodsId,
            @RequestParam Long reduceStock
    ){
        inventoryService.reduceStock(goodsId, reduceStock);
        return ResponseEntity.ok("재고 감소 완료");
    }

    // 유통기한 만료 상품 조회
    @GetMapping("/expiring-soon")
    public ResponseEntity<List<InventoryDTO>>getExpiringSoonItems(){
        List<InventoryDTO> list = inventoryService.getExpiringSoonItems();
        return ResponseEntity.status(200).body(list);
    }


    // 상품 입고 로직
    @PostMapping("/addStock")
    public ResponseEntity<String> addStock(
            @RequestParam Long goodsId,
            @RequestParam Long addStock

    ){
        inventoryService.addStock(goodsId, addStock);
        return ResponseEntity.ok("새로운 배치 추가 완료");
    }

}