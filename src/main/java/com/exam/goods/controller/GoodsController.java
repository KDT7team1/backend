package com.exam.goods.controller;


import com.exam.goods.dto.GoodsDTO;
import com.exam.goods.service.GoodsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000") 
public class GoodsController {
    GoodsService goodsService;

	public GoodsController(GoodsService goodsService) {
		super();
		this.goodsService = goodsService;
	}
 
	// 1. 상품 전체 조회
	@GetMapping("/findAll")
	public ResponseEntity<List<GoodsDTO>> findAll(){

		List<GoodsDTO> dtoList = goodsService.findAll();
		return ResponseEntity.status(200).body(dtoList);
	}


	// 2. 상품 상세보기
	@GetMapping("/findById/{id}")
	public ResponseEntity<GoodsDTO> findById(@PathVariable Long id){
		System.out.println(id);
		GoodsDTO dto =  goodsService.findById(id);

		if(dto == null){
			return ResponseEntity.status(404).body(null);
		}
		return  ResponseEntity.status(200).body(dto);
	}

	// 3. 상품 카테고리별 조회(대분류)
	@GetMapping("/findByCategory/{category_firstName}")
	public ResponseEntity<List<GoodsDTO>> findByFirstCategory(@PathVariable String category_firstName){

		List<GoodsDTO> list = goodsService.getGoodsByFirstCategory(category_firstName);
		return ResponseEntity.status(200).body(list);
	}

	// 4. 상품 카테고리별 조회(소분류)
	@GetMapping("/findByCategory/{category_firstName}/{category_secondName}")
	public ResponseEntity<List<GoodsDTO>> findByFirstCategory(@PathVariable String category_firstName, @PathVariable String category_secondName){

		List<GoodsDTO> list = goodsService.getGoodsBySecondCategory(category_firstName, category_secondName);
		return ResponseEntity.status(200).body(list);
	}

	

}
