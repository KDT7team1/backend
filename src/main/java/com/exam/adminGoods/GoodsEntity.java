package com.exam.adminGoods;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Table(name = "goods")
public class GoodsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 (AUTO_INCREMENT)
    private Long goods_id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @Column(nullable = false, length = 100) // 필수 값 & 최대 길이 지정
    private String goods_name;

    @Column(nullable = false)
    private Long goods_price;

    @Column(columnDefinition = "TEXT") // 긴 텍스트 저장 가능
    private String goods_description;

    @Column(nullable = false)
    private Long goods_stock;

    private String goods_image; // 이미지 URL 또는 경로 저장

    @Column(updatable = false) // 생성일은 수정 불가능
    private LocalDateTime goods_created_at;

    private LocalDateTime goods_updated_at;

    // 조회수 기본값 0
    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long goods_views;

    // 주문 수 기본값 0
    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long goods_orders;

    // 엔티티 저장 전 자동 실행
    @PrePersist
    protected void onCreate() {
        this.goods_created_at = LocalDateTime.now();
        this.goods_updated_at = LocalDateTime.now();
    }

    // 엔티티 업데이트 전 자동 실행
    @PreUpdate
    protected void onUpdate() {
        this.goods_updated_at = LocalDateTime.now();
    }
}
