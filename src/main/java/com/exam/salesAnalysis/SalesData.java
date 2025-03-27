package com.exam.salesAnalysis;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "sales_data")
public class SalesData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sales_id")
    Long salesId;

    @Column(name = "orders_id")
    Long ordersId;

    @Column(name = "goods_id")
    Long goodsId;

    @Column(name = "sale_amount")
    Long saleAmount;

    @Column(name = "sale_price")
    Long salePrice;

    @Column(name = "sale_date")
    LocalDateTime saleDate;
}
