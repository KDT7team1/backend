package com.exam.salesAnalysis;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SalesProductDTO {

    String productName; // 상품명
    Long totalAmount; // 총 판매수량
    Long totalPrice; // 총 매출금액
    Long salesDiff; // 매출 차이

}
