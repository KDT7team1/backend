package com.exam.goods;

import com.exam.Inventory.InventoryRepository;
import com.exam.cartAnalysis.repository.AssociationRulesRepository;
import com.exam.category.Category;
import com.exam.category.CategoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

    private final AssociationRulesRepository associationRulesRepository;
    GoodsRepository goodsRepository;
    CategoryRepository categoryRepository;
    InventoryRepository inventoryRepository;

    public GoodsServiceImpl(GoodsRepository goodsRepository,
                            CategoryRepository categoryRepository,
                            InventoryRepository inventoryRepository, AssociationRulesRepository associationRulesRepository) {
        super();
        this.goodsRepository = goodsRepository;
        this.categoryRepository = categoryRepository;
        this.inventoryRepository = inventoryRepository;
        this.associationRulesRepository = associationRulesRepository;
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
                            .goods_orders(item.getGoods_orders())
                            .originalPrice(item.getOriginalPrice())
                            .discountRate(item.getDiscountRate())
                            .discountEndAt(item.getDiscountEndAt())
                            .build();
                    return dto;

                }).collect(Collectors.toList());
        return goodsList;
    }

    // 2. 상품 상세보기
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
                .goods_orders(goods.getGoods_orders())
                .originalPrice(goods.getOriginalPrice())
                .discountRate(goods.getDiscountRate())
                .discountEndAt(goods.getDiscountEndAt())
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
                            .goods_orders(item.getGoods_orders())
                            .originalPrice(item.getOriginalPrice())
                            .discountRate(item.getDiscountRate())
                            .discountEndAt(item.getDiscountEndAt())
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
                            .goods_orders(item.getGoods_orders())
                            .originalPrice(item.getOriginalPrice())
                            .discountRate(item.getDiscountRate())
                            .discountEndAt(item.getDiscountEndAt())
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
                .goods_orders(dto.getGoods_orders())
                .originalPrice(dto.getOriginalPrice())
                .discountRate(dto.getDiscountRate())
                .discountEndAt(dto.getDiscountEndAt())
                .build();

        goodsRepository.save(goodsEntity);
    }

    @Override
    public void updateGoodsPrice(Long id, Long newPrice) {
        Goods goods = goodsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다: " + id));
        goods.setGoods_price(newPrice);
        goodsRepository.save(goods);
    }

    @Override
    public void applyDiscount(Long id, int discountRate, int period) {
        Goods goods = goodsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품 ID: " + id));

        if(goods.getOriginalPrice() == null){
            goods.setOriginalPrice(goods.getGoods_price());
        }

        Long originalPrice = goods.getOriginalPrice();
        Long discountPrice = Math.round(originalPrice * (1 - discountRate / 100.0));
        // 할인율 적용

        goods.setOriginalPrice(originalPrice);

        goods.setGoods_price(discountPrice);
        goods.setDiscountRate(discountRate);



        goods.setDiscountEndAt(LocalDateTime.now().plusDays(period)); // 일단 7일 할인기간


        goods.setGoods_updated_at(LocalDateTime.now());

        goodsRepository.save(goods);
    }

    @Override
    public void cancelDiscount(Long id) {
        Goods goods = goodsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품 ID"));

        if(goods.getOriginalPrice() != null){
            goods.setGoods_price(goods.getOriginalPrice());
            goods.setOriginalPrice(null);
            goods.setDiscountRate(null);
            goods.setDiscountEndAt(null);
            goods.setGoods_updated_at(LocalDateTime.now());
            goodsRepository.save(goods);
        }
    }

    // 연관상품 가져오기
    @Override
    public List<Goods> getRecommendedGoods(String subName) {
        List<String>  relatedSubNames = associationRulesRepository.findTopRelatedSubNames(subName);
        List<Goods> recommendations = new ArrayList<>();
        Set<Long> seenGoodsIds = new HashSet<>();

        for(String sub:relatedSubNames){
            List<Goods> randomGoods = goodsRepository.findRandomGoodsBySubName(sub,PageRequest.of(0, 3));
            for (Goods goods : randomGoods) {
                if (seenGoodsIds.add(goods.getGoods_id())) {
                    recommendations.add(goods);
                }
            }
        }
        return recommendations;
    }

    @Override
    public String getsubNameOfGoods(Long goodsId) {
        return  goodsRepository.findSubNameByGoodsId(goodsId);
    }


}
