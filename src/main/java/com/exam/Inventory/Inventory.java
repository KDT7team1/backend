package com.exam.Inventory;

import com.exam.goods.Goods;
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
    @Column(name = "batch_id")
    Long batchId;   // 재고 ID

    @ManyToOne
    @JoinColumn(name = "goods_id")
    Goods goods;

    @Column(name = "stock_quantity", nullable = false)
    Long stockQuantity; // 재고수량

    @Column(name = "stock_status", nullable = false)
    String stockStatus; // 재고 상태 ( 예: 부족, 충분)

    @Column(name = "stock_updated_at")
    LocalDateTime stockUpdateAt;    // 재고 업데이트 시간

    @Column(name = "expiration_date")
    LocalDateTime expirationDate;
}
