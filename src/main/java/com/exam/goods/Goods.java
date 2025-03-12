package com.exam.goods;

import com.exam.userGoods.entity.SubCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Table(name = "goods")
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long goods_id; // 상품 아이디

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

    //Long category_id;  // 상품 카테고리 아이디
    String goods_name; // 상품 이름
    Long goods_price;  // 상품 가격
    String goods_description; // 상품 설명
    Long goods_stock;         // 상품 재고
    String goods_image;       // 상품 이미지



    @CreationTimestamp
    @Column(updatable = false)
    LocalDateTime goods_created_at;  // 상품 등록 시간

    @UpdateTimestamp
    @Column(insertable = false)
    LocalDateTime goods_updated_at;  // 상품 수정 시간

    Long goods_views;  // 상품 조회수
    Long goods_orders; // 상품 주문수

}
