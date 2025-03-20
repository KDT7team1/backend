package com.exam.disposal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class DisposalDTO {

    Long disposalId; // 폐기번호

    @NotNull
    Long goodsId; // 상품번호

    @NotNull
    @Positive
    Long disposedQuantity; // 폐기수량

    LocalDateTime disposedAt; // 폐기일시

}
