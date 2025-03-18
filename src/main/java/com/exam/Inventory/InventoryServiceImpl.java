package com.exam.Inventory;


import com.exam.goods.Goods;
import com.exam.goods.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    private static final Long STOCK_THRESHOLD = 5L; // 재고 임계값 설정

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    GoodsRepository goodsRepository;

    // 1. 재고 테이블 전체 조회
    @Override
    public List<InventoryDTO> findAll() {
        List<InventoryDTO> inventoryList = inventoryRepository.findAll().stream()
                .map((item) -> {
                    InventoryDTO dto = InventoryDTO.builder()
                            .goodsId(item.getGoods().getGoods_id())
                            .goodsName(item.getGoods().getGoods_name())
                            .stockQuantity(item.getStockQuantity())
                            .stockStatus(item.getStockStatus())
                            .inventoryId(item.getInventoryId())
                            .stockUpdateAt(item.getStockUpdateAt())
                            .build();
                    return dto;
                }).collect(Collectors.toList());
        return inventoryList;
    }


    // 2. 특정 상품의 재고 정보를 조회
    @Override
    public InventoryDTO getInventory(Long goodsId) {
        Inventory inventory = inventoryRepository.findByGoodsId(goodsId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품의 재고 정보를 찾을 수 없습니다."));

        InventoryDTO inventoryDTO = InventoryDTO.builder()
                .goodsId(inventory.getGoods().getGoods_id())
                .goodsName(inventory.getGoods().getGoods_name())
                .stockQuantity(inventory.getStockQuantity())
                .stockStatus(inventory.getStockStatus())
                .inventoryId(inventory.getInventoryId())
                .stockUpdateAt(inventory.getStockUpdateAt())
                .build();

        return inventoryDTO;
    }


    // 3. 재고 수량 업데이트
    @Override
    public void updateInventory(Long goodsId) {
        Optional<Long> stock =  goodsRepository.findStockByGoodsId(goodsId);
        System.out.println("조회된 stock 값: " + stock);

        if(stock.isPresent()){
            Long goodsStock = stock.get();

            Goods goods = goodsRepository.findById(goodsId)
                    .orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다: " + goodsId));

            Inventory inventory = new Inventory();
            inventory.setGoods(goods);
            inventory.setStockQuantity(goodsStock);
            inventory.setStockStatus(goodsStock >= 5 ? "정상" : "재고부족");
            inventory.setStockUpdateAt(LocalDateTime.now());

            inventoryRepository.save(inventory); // 테이블에 저장하기
            System.out.println("인벤토리 업데이트 완료: " + inventory);


        }else{
            System.out.println("인벤토리 업데이트 실패");
        }
    }



}
