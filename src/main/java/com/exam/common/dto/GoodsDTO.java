package com.exam.common.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class GoodsDTO {
	Long goods_id;     // 상품 아이디
	Long category_id;  // 상품 카테고리 아이디
	String goods_name; // 상품 이름
	Long goods_price;  // 상품 가격
	String goods_description;  // 상품 설명
	Long goods_stock;   // 상품 재고
	String goods_image; // 상품 이미지
	LocalDateTime goods_updated_at; // 상품 등록 시간
	LocalDateTime goods_created_at; // 상품 수정 시간
	Long goods_views;   // 상품 조회수
	Long goods_orders;  // 상품 주문수
}
