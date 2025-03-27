package com.exam.orderRequest;

import com.exam.goods.Goods;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_request")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class OrderRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "goods_id", nullable = false)
    private Goods goods;

    private Long orderQuantity;

    private LocalDateTime orderTime;

    private String status;  // 예: "대기", "입고완료"

    private LocalDateTime scheduledTime;


}
