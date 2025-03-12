package com.exam.cartAnalysis.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Entity
@Table(name="association_rules")
public class AssociationRules {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long rule_id;

	String itemset_a;
	String itemset_b;

	Double support;
	Double confidence;
	Double lift;

	@CreationTimestamp
	@Column(updatable = false)
	LocalDateTime created_at;  // 상품 수정 시간



}
