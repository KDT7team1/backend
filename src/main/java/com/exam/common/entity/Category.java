package com.exam.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Entity
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long category_id;

	@Column(name="first_name")
	String firstName;

	@Column(name="second_name")
	String secondName;

	@OneToMany(mappedBy = "category")
	private List<Goods> goodsList;
}
