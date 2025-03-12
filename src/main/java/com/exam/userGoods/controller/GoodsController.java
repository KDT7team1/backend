package com.exam.userGoods.controller;


import com.exam.userGoods.dto.GoodsDTO;
import com.exam.userGoods.service.GoodsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products") // 해당 컨트롤러의 모든
public class GoodsController {
    GoodsService goodsService;

	public GoodsController(GoodsService goodsService) {
		super();
		this.goodsService = goodsService;
	}


	// 1. 상품 상세보기
	@GetMapping("/findById/{id}")
	public ResponseEntity<GoodsDTO> findById(@PathVariable Long id){
		System.out.println(id);
		GoodsDTO dto =  goodsService.findById(id);

		if(dto == null){
			return ResponseEntity.status(404).body(null);
		}
		return  ResponseEntity.status(200).body(dto);
	}



}
