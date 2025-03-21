package com.exam.cartAnalysis.controller;

import com.exam.cartAnalysis.entity.AssociationRules;
import com.exam.cartAnalysis.repository.AssociationRulesRepository;
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

	public AssociationRulesController(AssociationRulesRepository associationRulesRepository) {
		super();
		this.associationRulesRepository = associationRulesRepository;
	}

	@GetMapping
	public List<AssociationRules> getRules(@RequestParam(required = false) String period){
		if(period == null || period.equals("all")){
			return associationRulesRepository.findAll();
		}else{
			return associationRulesRepository.findByPeriodLabel(period);
		}

	}

}
