package com.exam.shop;

import com.exam.goods.Goods;
import com.exam.goods.GoodsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopServiceImpl implements ShopService {

    ShopRepository shopRepository;

    public ShopServiceImpl(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @Override
    public Page<GoodsDTO> getGoods(Long category, String search, Pageable pageable) {
        // 필터링 & 검색 추가
        Page<Goods> goodsPage = shopRepository.findAllByFilters(category, search, pageable);

        // Goods → GoodsDTO 변환
        List<GoodsDTO> goodsDTOList = goodsPage.getContent().stream().map(item ->
                GoodsDTO.builder()
                        .goods_id(item.getGoods_id())
                        .category_id(item.getCategory().getCategory_id())
                        .goods_name(item.getGoods_name())
                        .goods_price(item.getGoods_price())
                        .goods_description(item.getGoods_description())
                        .goods_image(item.getGoods_image())
                        .goods_stock(item.getGoods_stock())
                        .goods_created_at(item.getGoods_created_at())
                        .goods_updated_at(item.getGoods_updated_at())
                        .goods_orders(item.getGoods_orders())
                        .originalPrice(item.getOriginalPrice())
                        .discountRate(item.getDiscountRate())
                        .discountEndAt(item.getDiscountEndAt())
                        .build()
        ).collect(Collectors.toList());

        return new PageImpl<>(goodsDTOList, pageable, goodsPage.getTotalElements());
    }

    @Override
    public List<GoodsDTO> findAllInStockGoods() {
        // 재고가 있는 모든 상품 조회
        List<GoodsDTO> goodsDTOList = shopRepository.findAllInStockGoods().stream().map((item) -> {
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
                    .goods_orders(item.getGoods_orders())
                    .originalPrice(item.getOriginalPrice())
                    .discountRate(item.getDiscountRate())
                    .discountEndAt(item.getDiscountEndAt())
                    .build();
            return dto;
        }).collect(Collectors.toList());
        return goodsDTOList;
    }

    @Override
    public List<GoodsDTO> getIsDiscountedList() {
        // 할인 중인 상품 조회
        LocalDateTime now = LocalDateTime.now();
        List<GoodsDTO> goodsDTOList = shopRepository.findDiscountedGoodsInStock(now).stream()
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
                            .goods_orders(item.getGoods_orders())
                            .originalPrice(item.getOriginalPrice())
                            .discountRate(item.getDiscountRate())
                            .discountEndAt(item.getDiscountEndAt())
                            .build();
                    return dto;
                }).collect(Collectors.toList());
        return goodsDTOList;
    }
}
