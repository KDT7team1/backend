package com.exam.adminGoods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class GoodsController {

    GoodsService goodsService;

    @Autowired
    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    // 단일 상품 등록 API
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody GoodsDTO dto) {
        System.out.println("Received category_id: " + dto.getCategory_id());
        goodsService.save(dto);
        return  ResponseEntity.ok("상품이 저장되었습니다.");
    }

    //상품 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody Long goodsId) {
        goodsService.delete(goodsId);
        return ResponseEntity.ok("상품이 삭제되었습니다.");
    }




}
