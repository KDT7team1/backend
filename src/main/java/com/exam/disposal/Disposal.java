package com.exam.disposal;

import com.exam.Inventory.Inventory;
import com.exam.goods.Goods;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Entity
public class Disposal {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long disposal_id; // 상품 아이디

    @ManyToOne
    @JoinColumn(name = "goods_id")
    private Goods goods;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Inventory inventory;

    Long disposed_quantity;
    String disposal_reason; // 상품 설명


    @CreationTimestamp
    @Column(updatable = false )
    LocalDateTime disposed_at;  // 상품 수정 시간


}
