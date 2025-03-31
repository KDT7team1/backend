package com.exam.orderRequest;

import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder

public class OrderDTO {

    private Long orderId;
    private Long goodsId;
    private String goodsName;
    private Long orderQuantity;
    private LocalDateTime orderTime;
    private String status;
    private LocalDateTime scheduledTime;

}
