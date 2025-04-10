package com.exam.cartAnalysis.repository;

import com.exam.cartAnalysis.entity.AssociationTimeRules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociationTimeRulesRepository extends JpaRepository<AssociationTimeRules, Long>{

    
}

