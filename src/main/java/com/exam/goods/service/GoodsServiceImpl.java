package com.exam.goods.service;



import com.exam.goods.Category;
import com.exam.goods.repository.CategoryRepository;
import com.exam.goods.Goods;
import com.exam.goods.GoodsDTO;
import com.exam.goods.repository.GoodsRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class GoodsServiceImpl implements GoodsService {
	GoodsRepository goodsRepository;



	CategoryRepository categoryRepository;

	public GoodsServiceImpl(GoodsRepository goodsRepository, CategoryRepository categoryRepository) {
		this.goodsRepository = goodsRepository;
		this.categoryRepository = categoryRepository;
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




	//상품 저장
	@Override
	public void save(GoodsDTO dto) {
		System.out.println("GoodsDTO2:" + dto);
		if (dto.getCategory_id() == null) {
			throw new IllegalArgumentException("category_id는 null이 될 수 없습니다.");
		}
		// 1. category_id를 사용하여 카테고리 조회
		Category categoryEntity = categoryRepository.findById(dto.getCategory_id())
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 ID: " + dto.getCategory_id()));

		// 2. GoodsEntity 생성 및 저장
		Goods goodsEntity = Goods.builder()
				.goods_id(dto.getGoods_id())
				.category(categoryEntity)
				.goods_name(dto.getGoods_name())
				.goods_price(dto.getGoods_price())
				.goods_description(dto.getGoods_description())
				.goods_stock(dto.getGoods_stock())
				.goods_image(dto.getGoods_image())
				.goods_created_at(dto.getGoods_created_at())
				.goods_updated_at(dto.getGoods_updated_at())
				.goods_views(dto.getGoods_views())
				.goods_orders(dto.getGoods_orders())
				.build();

		goodsRepository.save(goodsEntity);
	}



}
