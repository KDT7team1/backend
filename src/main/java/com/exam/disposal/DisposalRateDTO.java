package com.exam.disposal;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class DisposalRateDTO {

    String subName;           // 상품명
    Long stockQuantity;       // 입고 수량 (이번달)
    Long saleAmount;          // 판매 수량 (이번달)
    Long disposedQuantity;    // 폐기 수량 (이번달)
    double disposalRate;      // 폐기율 = 폐기 / 입고 * 100

}
