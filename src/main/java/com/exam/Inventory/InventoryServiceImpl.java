package com.exam.Inventory;


import com.exam.alert.SseService;
import com.exam.category.SubCategory;
import com.exam.goods.Goods;
import com.exam.goods.GoodsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {

    private static final Long STOCK_THRESHOLD = 5L; // 재고 임계값 설정


    @Autowired
    private InventoryRepository inventoryRepository;


    @Autowired
    GoodsRepository goodsRepository;
    @Autowired
    private SseService sseService;


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
                inventory.setStockQuantity(0L);// 현재 배치에는 남은 수량이 0개임
                inventory.setStockStatus("재고부족"); // 현재배치 상태는 재고부족
            }
            inventoryRepository.save(inventory);
            log.info("재고 처리 - batchId: {}, 남은 수량: {}", inventory.getBatchId(), inventory.getStockQuantity());

        }
        // 모든 재고 처리 후 전체 재고 수량 확인
        long totalRemaining = list.stream()
                .mapToLong(Inventory::getStockQuantity)
                .sum();

        String goodsName = list.get(0).getGoods().getGoods_name(); // 상품명

        if (totalRemaining == 0) {
            log.info("📢 품절 알림 보내기");
            sseService.sendNotification("admin", "품절", goodsName + " 상품이 품절입니다!");
        } else if (totalRemaining < 5) {
            log.info("📢 재고 부족 알림 보내기");
            sseService.sendNotification("admin", "재고부족", goodsName + " 재고가 5개 미만입니다!");
        }


        updateGoodsStock(goodsId);


    }


    /* 재고 증가 로직 : 새 배치 단위로 추가해야됨 */
    @Override
    @Transactional
    public void addStock(Long goodsId, Long addStock) {
        Optional<Goods> goodsOpt = goodsRepository.findById(goodsId);

        if (goodsOpt.isPresent()) {
            Goods goods = goodsOpt.get();
            SubCategory subCategory = goods.getSubCategory();

            // 유통기한 가져오기
            Integer expirationPeriod = subCategory.getExpirationPeriod();
            LocalDateTime expirationDate = null;

            if (expirationPeriod != null && expirationPeriod > 0) {
                expirationDate = LocalDateTime.now().plusDays(expirationPeriod);
            }

            Inventory newInventory = new Inventory();
            newInventory.setGoods(goods);
            newInventory.setStockQuantity(addStock);
            newInventory.setStockStatus(addStock >= 5 ? "정상" : "재고부족");
            newInventory.setStockUpdateAt(LocalDateTime.now());
            newInventory.setExpirationDate(expirationDate); // null이면 유통기한 없는 상품
            newInventory.setInitialStockQuantity(addStock);

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


    // 유통기한 3일전 상품 조회
    @Override
    public List<InventoryDTO> getExpiringSoonItems() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.plusDays(3); // 3일 더하기

        List<Inventory> list = inventoryRepository.findExpiringSoonItems(now, limit);

        return list.stream().map(i -> InventoryDTO.builder()
                .batchId(i.getBatchId())
                .goodsId(i.getGoods().getGoods_id())
                .goodsPrice(i.getGoods().getGoods_price())
                .goodsName(i.getGoods().getGoods_name())
                .expirationDate(i.getExpirationDate())
                .stockQuantity(i.getStockQuantity())
                .stockStatus(i.getStockStatus())
                .build()
        ).toList();

    }


}
