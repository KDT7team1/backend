package com.exam.Inventory;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class InventoryDTO {

    Long inventoryId;   // 재고 ID
    Long goodsId;       // 상품 ID
    String goodsName;   // 상품명
    Long stockQuantity; // 재고 수량
    String stockStatus; // 재고 상태
    LocalDateTime stockUpdateAt;    // 재고 업데이트 시간

}
