package com.exam.Inventory;


import com.exam.goods.Goods;
import com.exam.goods.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
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
                            .batchId(item.getBatchId())
                            .stockUpdateAt(item.getStockUpdateAt())
                            .expirationDate(item.getExpirationDate())
                            .build();
                    return dto;
                }).collect(Collectors.toList());
        return inventoryList;
    }

    // 2. 특정 상품의 재고 정보를 조회
    @Override
    public InventoryDTO getInventory(Long batchId) {
        Inventory inventory = inventoryRepository.findById(batchId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품의 재고 정보를 찾을 수 없습니다."));

        InventoryDTO inventoryDTO = InventoryDTO.builder()
                .goodsId(inventory.getGoods().getGoods_id())
                .goodsName(inventory.getGoods().getGoods_name())
                .stockQuantity(inventory.getStockQuantity())
                .stockStatus(inventory.getStockStatus())
                .batchId(inventory.getBatchId())
                .stockUpdateAt(inventory.getStockUpdateAt())
                .expirationDate(inventory.getExpirationDate())
                .build();

        return inventoryDTO;
    }


    // 3. 재고 수량 업데이트
    @Override
    public void updateStockByBatchId(Long batchId, Long newStock) {
        Optional<Inventory> inventoryOpt =  inventoryRepository.findById(batchId); // 일치하는 컬럼 찾기

        if(inventoryOpt.isPresent()) {
            Inventory inventory =  inventoryOpt.get(); // 컬럼 가져오고
            inventory.setStockQuantity(newStock);
            inventory.setStockStatus(newStock == 0 ? "폐기" : newStock >= 5? "정상" : "재고부족");
            inventory.setStockUpdateAt(LocalDateTime.now());
            inventoryRepository.save(inventory);
            System.out.println("배치 ID " + batchId + " 재고 업데이트 완료: " + newStock);

            Long goodsId = inventory.getGoods().getGoods_id();

            updateGoodsStock(goodsId);

        } else {
            throw new RuntimeException("해당 배치를 찾을 수 없습니다.");
        }
    }

    // 상품 재고 수량 변경 (업데이트) => 재고 테이블도 동시에 수정됨
    @Override
    @Transactional
    public void updateGoodsStock(Long goodsId) {
        Optional<Goods> goodsOpt = goodsRepository.findById(goodsId);
        if(goodsOpt.isPresent()) {
            // 1. 상품 테이블에 재고 수정
            Goods goods = goodsOpt.get();

            // 총 재고수 계산
            long totalStock = inventoryRepository.findByGoodsId(goodsId)
                    .stream()
                    .mapToLong(Inventory::getStockQuantity)
                    .sum();

            goods.setGoods_stock(totalStock); // 전달받은 재고로 수정하기
            goodsRepository.save(goods);

            System.out.println("상품 ID " + goodsId + " 총 재고 업데이트 완료: " + totalStock);
        }
        else {
            throw new RuntimeException("해당 상품을 찾을 수 없습니다.");
        }
    }

    /* 재고 감소 로직*/
    @Override
    @Transactional
    public void reduceStock(Long goodsId, Long reduceStock){
        List<Inventory> list = inventoryRepository.findByGoodsId(goodsId);
        list.sort(Comparator.comparing(Inventory::getExpirationDate)); //  유통기한순으로 정렬

        long remainingStock = reduceStock;

        for (Inventory inventory : list) {
            if(remainingStock  < 0) break;

            // 현재 수량이 더 많은 경우에는 그냥 빼면 됨
            if(inventory.getStockQuantity() > remainingStock ){
                inventory.setStockQuantity(inventory.getStockQuantity() - remainingStock );
                inventory.setStockStatus(inventory.getStockQuantity() >= 5 ? "정상": "재고부족" );
                remainingStock  = 0;
            } else {
                remainingStock -= inventory.getStockQuantity(); // 11 - 10 = 1 개 남음
                inventory.setStockQuantity(0L);// 현재 배치에는 남은 수량이0개임
                inventory.setStockStatus("재고부족"); // 현재배치 상태는 재고부좃
            }
            inventoryRepository.save(inventory);
        }
    }

    /* 재고 증가 로직 : 새 배치 단위로 추가해야됨 */
    @Override
    @Transactional
    public void addStock(Long goodsId, Long addStock, LocalDateTime expirationDate) {
        Optional<Goods> goodsOpt = goodsRepository.findById(goodsId);

        if (goodsOpt.isPresent()) {
            Goods goods = goodsOpt.get();

            Inventory newInventory = new Inventory();
            newInventory.setGoods(goods);
            newInventory.setStockQuantity(addStock);
            newInventory.setStockStatus(addStock >= 5 ? "정상" : "재고부족");
            newInventory.setStockUpdateAt(LocalDateTime.now());
            newInventory.setExpirationDate(expirationDate);

            inventoryRepository.save(newInventory);

            long updatedTotalStock  = inventoryRepository.findByGoodsId(goodsId)
                    .stream()
                    .mapToLong(Inventory::getStockQuantity)
                    .sum();

            goods.setGoods_stock(updatedTotalStock ); // 전달받은 재고로 수정하기
            goodsRepository.save(goods);

            System.out.println("새로운 배치 추가 완료: " + addStock);
            System.out.println("상품 ID " + goodsId + " 의 총 재고 업데이트 완료: " + updatedTotalStock);
        } else {
            throw new RuntimeException("해당 상품을 찾을 수 없습니다.");
        }

    }

    @Override
    public List<InventoryDTO> getExpiringSoonItems() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.plusDays(3); // 3일 더하기

        List<Inventory> list = inventoryRepository.findExpiringSoonItems(now, limit);

        return list.stream().map(i -> InventoryDTO.builder()
                .batchId(i.getBatchId())
                .goodsId(i.getGoods().getGoods_id())
                .goodsName(i.getGoods().getGoods_name())
                .expirationDate(i.getExpirationDate())
                .stockQuantity(i.getStockQuantity())
                .stockStatus(i.getStockStatus())
                .build()
        ).toList();

    }


}
