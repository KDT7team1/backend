package com.exam.cartAnalysis.entity;

import com.exam.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Entity
@Table(name = "orders")
public class Orders {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long ordersId;

	@ManyToOne
	@JoinColumn(name="member_no")
	Member member;

	@CreationTimestamp
	LocalDateTime ordersDate;

}
