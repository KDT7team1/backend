package com.exam.disposal;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ValueGenerationType;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "disposal")
public class Disposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disposal_id")
    Long disposalId; // 폐기번호
    
    @Column(name = "goods_id")
    Long goodsId; // 상품번호
    
    @Column(name = "disposed_quantity")
    Long disposedQuantity; // 폐기수량
    
    @Column(name = "disposed_at")
    LocalDateTime disposedAt; // 폐기일시

}
