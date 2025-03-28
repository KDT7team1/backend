package com.exam.goods;

import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder

public class GoodsDTO {

    private Long goods_id;
    private Long category_id;
    private Long sub_category_id;
    private String goods_name;
    private Long goods_price;
    private String goods_description;
    private Long goods_stock;
    private String goods_image;
    private LocalDateTime goods_created_at;
    private LocalDateTime goods_updated_at;
    private Long goods_orders;
    private Long originalPrice;
    private Integer discountRate;
    private LocalDateTime discountEndAt;


}
