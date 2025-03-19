package com.exam.Inventory;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/inventory")
@RestController
public class InventoryController {

    @Autowired
    InventoryServiceImpl inventoryService;

    // 1. 재고테이블 전체 조히
    @GetMapping("/findAll")
    public ResponseEntity<List<InventoryDTO>> findAll(){
        List<InventoryDTO> inventoryList = inventoryService.findAll();
        return ResponseEntity.status(200).body(inventoryList);
    }

    // 2. 특정 상품의 재고 정보를 조회
    @GetMapping("/findById/{goodsId}")
    public ResponseEntity<InventoryDTO> getInventory(@PathVariable Long goodsId){
        InventoryDTO dto =  inventoryService.getInventory(goodsId);

        if(dto == null){
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.status(200).body(dto);
    }


    // 3. 재고 수량 업데이트 => 있어야 하는지 의문
//    @GetMapping("/update/{goodsId}")
//    public ResponseEntity<String> updateInventory(@PathVariable Long goodsId){
//        System.out.println("재고 업데이트 API 호출됨: goodsId = " + goodsId);
//        inventoryService.updateInventory(goodsId);
//
//        return ResponseEntity.ok("success");
//    }



}
