package com.exam.userGoods.service;

import com.exam.userGoods.dto.GoodsDTO;
import com.exam.userGoods.entity.Goods;
import com.exam.userGoods.repository.GoodsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class GoodsServiceImpl implements GoodsService {
	GoodsRepository goodsRepository;

	public GoodsServiceImpl(GoodsRepository goodsRepository) {
		super();
		this.goodsRepository = goodsRepository;
	}

	// 1. 전체 목록 조회
	@Override
	public List<GoodsDTO> findAll() {
		List<GoodsDTO> goodsList = goodsRepository.findAll().stream()
				.map((item)->{
					GoodsDTO dto = GoodsDTO.builder()
							.goods_id(item.getGoods_id())
							.category_id(item.getCategory().getCategory_id())
							.goods_name(item.getGoods_name())
							.goods_price(item.getGoods_price())
							.goods_description(item.getGoods_description())
							.goods_image(item.getGoods_image())
							.goods_stock(item.getGoods_stock())
							.goods_created_at(item.getGoods_created_at())
							.goods_updated_at(item.getGoods_updated_at())
							.goods_views(item.getGoods_views())
							.goods_orders(item.getGoods_orders())
							.build();
					return dto;

				}).collect(Collectors.toList());
		return goodsList;
	}


	// 2. 상품 상제보기
	@Override
	public GoodsDTO findById(Long id){
		Goods goods =  goodsRepository.findById(id).orElse(null);

		if (goods == null) {
			return null; // 데이터가 없으면 null 반환
		}

		GoodsDTO goodsDTO = GoodsDTO.builder()
							.goods_id(goods.getGoods_id())
							.category_id(goods.getCategory().getCategory_id())
							.goods_name(goods.getGoods_name())
							.goods_price(goods.getGoods_price())
							.goods_description(goods.getGoods_description())
							.goods_image(goods.getGoods_image())
							.goods_stock(goods.getGoods_stock())
							.goods_created_at(goods.getGoods_created_at())
							.goods_updated_at(goods.getGoods_updated_at())
							.goods_views(goods.getGoods_views())
							.goods_orders(goods.getGoods_orders())
							.build();
		return goodsDTO;
	}

	// 3. 상품 카테고리별 조회하기 (대분류)
	@Override
	public List<GoodsDTO> getGoodsByFirstCategory(String firstName) {
		List<GoodsDTO> goodsDTOList = goodsRepository.findByFirstCategory(firstName).stream()
				.map((item)->{
					GoodsDTO dto = GoodsDTO.builder()
							.goods_id(item.getGoods_id())
							.category_id(item.getCategory().getCategory_id())
							.goods_name(item.getGoods_name())
							.goods_price(item.getGoods_price())
							.goods_description(item.getGoods_description())
							.goods_image(item.getGoods_image())
							.goods_stock(item.getGoods_stock())
							.goods_created_at(item.getGoods_created_at())
							.goods_updated_at(item.getGoods_updated_at())
							.goods_views(item.getGoods_views())
							.goods_orders(item.getGoods_orders())
							.build();
					return dto;
				}).collect(Collectors.toList());
		return goodsDTOList;
	}

	// 4. 상품 카테고리별 조회하기 (소분류)
	@Override
	public List<GoodsDTO> getGoodsBySecondCategory(String firstName, String secondName) {
		List<GoodsDTO> goodsDTOList = goodsRepository.findBySecondCategory(firstName, secondName).stream()
				.map((item)->{
					GoodsDTO dto = GoodsDTO.builder()
							.goods_id(item.getGoods_id())
							.category_id(item.getCategory().getCategory_id())
							.goods_name(item.getGoods_name())
							.goods_price(item.getGoods_price())
							.goods_description(item.getGoods_description())
							.goods_image(item.getGoods_image())
							.goods_stock(item.getGoods_stock())
							.goods_created_at(item.getGoods_created_at())
							.goods_updated_at(item.getGoods_updated_at())
							.goods_views(item.getGoods_views())
							.goods_orders(item.getGoods_orders())
							.build();
					return dto;
				}).collect(Collectors.toList());
		return goodsDTOList;
	}





}
