package com.exam.category;

import com.exam.goods.GoodsDTO;
import com.exam.goods.GoodsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories") // 해당 컨트롤러의 모든
public class CategoryController {
    GoodsService goodsService;

    public CategoryController(GoodsService goodsService) {
        super();
        this.goodsService = goodsService;
    }

    // 1. 상품 전체 조회
    @GetMapping("/findAll")
    public ResponseEntity<List<GoodsDTO>> findAll(){

        List<GoodsDTO> dtoList = goodsService.findAll();
        return ResponseEntity.status(200).body(dtoList);
    }

    // 2. 상품 카테고리별 조회(대분류)
    @GetMapping("/{firstName}")
    public ResponseEntity<List<GoodsDTO>> findByFirstCategory(@PathVariable String firstName){

        List<GoodsDTO> list = goodsService.getGoodsByFirstCategory(firstName);
        return ResponseEntity.status(200).body(list);
    }

    // 3. 상품 카테고리별 조회(소분류)
    @GetMapping("/{firstName}/{secondName}")
    public ResponseEntity<List<GoodsDTO>> findByFirstCategory(@PathVariable String firstName, @PathVariable String secondName){

        List<GoodsDTO> list = goodsService.getGoodsBySecondCategory(firstName, secondName);
        return ResponseEntity.status(200).body(list);
    }

}