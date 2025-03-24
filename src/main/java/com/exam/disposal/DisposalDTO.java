package com.exam.disposal;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class DisposalDTO {

    Long disposal_id;
    Long goods_id;
    String goods_name;
    Long batch_id;
    Long disposed_quantity;
    String disposal_reason;
    LocalDateTime disposed_at;

}
