package com.exam.userGoods.service;



import com.exam.common.dto.GoodsDTO;
import java.util.List;


public interface GoodsService {
	// 1. 상품 전체 보기
	List<GoodsDTO> findAll();

	// 2. 상품 상세보기
	GoodsDTO findById(Long id);

	// 3. 카테고리별 상품 보기 ( 대분류 : 식품,음료,생황용품 )
	List<GoodsDTO> getGoodsByFirstCategory(String firstName);

	// 4. 카테고리별 상품 보기 ( 소분류 : 과자, 아이스크림,.. )
	List<GoodsDTO> getGoodsBySecondCategory(String firstName, String secondName);



}
