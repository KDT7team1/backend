package com.exam.cartAnalysis.controller;

import com.exam.cartAnalysis.entity.AssociationRules;
import com.exam.cartAnalysis.entity.AssociationTimeRules;
import com.exam.cartAnalysis.repository.AssociationRulesRepository;
import com.exam.cartAnalysis.repository.AssociationTimeRulesRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/association") // 해당 컨트롤러의 모든
public class AssociationRulesController {

	AssociationRulesRepository associationRulesRepository;
	AssociationTimeRulesRepository associationTimeRulesRepository;

	public AssociationRulesController(AssociationRulesRepository associationRulesRepository,
									  AssociationTimeRulesRepository associationTimeRulesRepository) {
		super();
		this.associationRulesRepository = associationRulesRepository;
		this.associationTimeRulesRepository = associationTimeRulesRepository;
	}

	@GetMapping
	public List<AssociationRules> getRules(
			@RequestParam(required = false) String period,
			@RequestParam(required = false) String month){

		if ((period == null || period.equals("all")) && (month == null || month.equals("all"))) {
			return associationRulesRepository.findAll();
		} else if ((month == null || month.equals("all"))) {
			// 연도만 선택됨 → "2025"
			return associationRulesRepository.findByPeriodLabel(period);
		} else {
			// 연도 + 월 → "2025-03"
			String combined = period + "-" + month;
			return associationRulesRepository.findByPeriodLabel(combined);
		}

	}

	@GetMapping("/time")
	public ResponseEntity<List<AssociationTimeRules>> getAllTimeRules(){
		List<AssociationTimeRules> list = associationTimeRulesRepository.findAll();
		return ResponseEntity.status(200).body(list);
	}



}
