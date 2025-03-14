package com.exam.Inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    private static final Long STOCK_THRESHOLD = 5L; // 재고 임계값 설정

    @Autowired
    private InventoryRepository inventoryRepository;

    // 특정 상품의 재고 정보를 조회
    @Override
    public InventoryDTO getInventory(Long goodsId) {
        Inventory inventory = inventoryRepository.findByGoodsId(goodsId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품의 재고 정보를 찾을 수 없습니다."));

        return new InventoryDTO(inventory.getInventoryId(), inventory.inventoryId,
                inventory.getStockQuantity(), inventory.getStockStatus(), inventory.getStockUpdateAt());
    }

    // 재고 수량 업데이트
    @Override
    public void updateInventory(Long goodsId, Long stockQuantity) {
        Inventory inventory = inventoryRepository.findByGoodsId(goodsId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품의 재고 정보를 찾을 수 없습니다."));

        // 재고 수량 업데이트
        inventory.setStockQuantity(stockQuantity);

        // 재고 업데이트 시간 갱신
        inventory.setStockUpdateAt(java.time.LocalDateTime.now());

        // 변경된 재고 정볼를 DB에 저장
        inventoryRepository.save(inventory);

    }

    // 재고가 부족한 상품 목록 조회
    @Override
    public List<InventoryDTO> getLowStockItems() {
        // 재고가 부족한 상품을 입계값 이하로 설정 (예: 임계값 10)
        long threshold = 10;

        List<Inventory> lowStockItems = inventoryRepository.findAll().stream()
                .filter(item -> item.getStockQuantity() <= threshold)
                .collect(Collectors.toList());

        return lowStockItems.stream()
                .map(inventory -> new InventoryDTO(inventory.getInventoryId(), inventory.getGoodsId(),
                        inventory.getStockQuantity(), inventory.getStockStatus(), inventory.getStockUpdateAt()))
                .collect(Collectors.toList());
    }

}
