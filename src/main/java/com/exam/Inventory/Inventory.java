package com.exam.Inventory;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    Long inventoryId;   // 재고 ID

    @Column(name = "goods_id", nullable = false)
    Long goodsId;       // 상품번호

    @Column(name = "stock_quantity", nullable = false)
    Long stockQuantity; // 재고수량

    @Column(name = "stock_status", nullable = false)
    String stockStatus; // 재고 상태 ( 예: 부족, 충분)

    @Column(name = "stock_updated_at")
    LocalDateTime stockUpdateAt;    // 재고 업데이트 시간

}
