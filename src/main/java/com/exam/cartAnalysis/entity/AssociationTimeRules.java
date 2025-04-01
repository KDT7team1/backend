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
@Table(name="association_timerules")
public class AssociationTimeRules {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	String time_period;
	String itemset_a;
	String itemset_b;

	Double support;
	Double confidence;
	Double lift;

	@CreationTimestamp
	@Column(updatable = false)
	LocalDateTime created_at;

}
