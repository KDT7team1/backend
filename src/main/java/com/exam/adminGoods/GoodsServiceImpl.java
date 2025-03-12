package com.exam.adminGoods;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

    GoodsRepository goodsRepository;
    CategoryRepository categoryRepository;

    public GoodsServiceImpl(GoodsRepository goodsRepository, CategoryRepository categoryRepository) {
        this.goodsRepository = goodsRepository;
        this.categoryRepository = categoryRepository;
    }

    //상품 저장
    @Override
    public void save(GoodsDTO dto) {
        System.out.println("GoodsDTO2:" + dto);
        if (dto.getCategory_id() == null) {
            throw new IllegalArgumentException("category_id는 null이 될 수 없습니다.");
        }
        // 1. category_id를 사용하여 카테고리 조회
        CategoryEntity categoryEntity = categoryRepository.findById(dto.getCategory_id())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 ID: " + dto.getCategory_id()));

        // 2. GoodsEntity 생성 및 저장
        GoodsEntity goodsEntity = GoodsEntity.builder()
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

    //상품 조회
//    @Override
//    public GoodsDTO findById(Long goodsId) {
//
//        GoodsEntity entity = goodsRepository.findById(goodsId)
//                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. ID: " + goodsId));
//
//         return GoodsDTO.builder()
//                    .goods_id(entity.getGoods_id())
//                    .category_id(entity.getCategory().getCategory_id())
//                    .goods_name(entity.getGoods_name())
//                    .goods_price(entity.getGoods_price())
//                    .goods_description(entity.getGoods_description())
//                    .goods_stock(entity.getGoods_stock())
//                    .goods_image(entity.getGoods_image())
//                    .build();
//        }


    }




