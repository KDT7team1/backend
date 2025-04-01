package com.exam.cartAnalysis.repository;

import com.exam.cartAnalysis.entity.AssociationRules;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssociationRulesRepository extends JpaRepository<AssociationRules, Long>{
    List<AssociationRules> findByPeriodLabel(String periodLabel);

}

