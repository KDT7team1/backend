package com.exam.goods;

import com.exam.Inventory.Inventory;
import com.exam.Inventory.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.exam.goods.Goods;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

    GoodsRepository goodsRepository;
    CategoryRepository categoryRepository;
    InventoryRepository inventoryRepository;

    public GoodsServiceImpl(GoodsRepository goodsRepository,
                            CategoryRepository categoryRepository,
                            InventoryRepository inventoryRepository) {
        super();
        this.goodsRepository = goodsRepository;
        this.categoryRepository = categoryRepository;
        this.inventoryRepository = inventoryRepository;
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
        System.out.println("GoodsDTO2:" + dto); //GoodsDTO2가 맞는지?
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


    @Override
    public void updateGoodsStock(Long goodsId, Long newStock) {
        Optional<Goods> goodsOpt = goodsRepository.findById(goodsId);
        if(goodsOpt.isPresent()) {
            // 1. 상품 테이블에 재고 수정
            Goods goods = goodsOpt.get();
            goods.setGoods_stock(newStock); // 전달받은 재고로 수정하기
            goodsRepository.save(goods);

            // 2. 동시에 재고 테이블 재고 수정
            Optional<Inventory> invenOpt =  inventoryRepository.findByGoodsId(goodsId);

            if(invenOpt.isPresent()) {
                Inventory inventory = invenOpt.get();

                inventory.setStockQuantity(newStock);
                inventory.setStockStatus(newStock >= 5 ? "정상" : "재고부족");
                inventory.setStockUpdateAt(LocalDateTime.now());
                inventoryRepository.save(inventory);
            }else {

                Inventory newInventory = new Inventory();
                newInventory.setGoods(goods);
                newInventory.setStockQuantity(newStock);
                newInventory.setStockStatus(newStock >= 5 ? "정상" : "재고부족");
                newInventory.setStockUpdateAt(LocalDateTime.now());
            }
            System.out.println("상품 재고 및 인벤토리 업데이트 완료: " + newStock);
        }
        else {
            throw new RuntimeException("해당 상품을 찾을 수 없습니다.");
        }
    }

    //상품 수정
    @Override
    public void update(GoodsDTO dto) {

        // 예외처리
        if (dto.getGoods_id() == null) {
            throw new IllegalArgumentException("상품 ID는 null이 될 수 없습니다.");
        }

        // 기존 상품 조회
        Goods goods = goodsRepository.findById(dto.getGoods_id())
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다: " + dto.getGoods_id()));

        // 상품 정보 업데이트
        if (dto.getGoods_name() != null) goods.setGoods_name(dto.getGoods_name());
        if (dto.getGoods_price() != null) goods.setGoods_price(dto.getGoods_price());
        if (dto.getGoods_description() != null) goods.setGoods_description(dto.getGoods_description());
        if (dto.getGoods_stock() != null) goods.setGoods_stock(dto.getGoods_stock());

        // 이미지 업데이트
        if (dto.getGoods_image() != null && !dto.getGoods_image().isEmpty()) {
            goods.setGoods_image(dto.getGoods_image());
        }

        // 업데이트 시간 변경
        goods.setGoods_updated_at(LocalDateTime.now());

        // DB 저장
        goodsRepository.save(goods);
    }

    @Override
    public void delete(Long goodsId) {
        goodsRepository.deleteById(goodsId);
        System.out.println("상품 삭제 완료: " + goodsId);
    }

}




