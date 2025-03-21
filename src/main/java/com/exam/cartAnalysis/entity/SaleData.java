package com.exam.cartAnalysis.entity;

import com.exam.goods.Goods;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Entity
@Table(name="sale_data")
public class SaleData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long salesId;

	@ManyToOne
	@JoinColumn(name="orders_id")
	Orders orders;

	@ManyToOne
	@JoinColumn(name="goods_id")
	Goods goods;

	Long saleAmount;
	Long salePrice;

	@Column(updatable = false)
	LocalDateTime saleDate;

}
