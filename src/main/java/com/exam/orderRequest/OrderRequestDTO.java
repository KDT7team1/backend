package com.exam.orderRequest;

import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder

public class OrderRequestDTO {

    private Long orderId;
    private Long goodsId;
    private String goodsName;
    private String goodsImage; // 발주 리스트의 이미지 출력을 위해 추가
    private Long orderQuantity;
    private LocalDateTime orderTime;
    private String status;
    private LocalDateTime scheduledTime;

}
