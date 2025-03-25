package com.exam.disposal;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class DisposalStatsDTO {

    String subCategoryName; // 카테고리 이름
    Long totalQuantity;     // 총 폐기량

}
