package com.exam.cartAnalysis.repository;

import com.exam.cartAnalysis.entity.AssociationRules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssociationRulesRepository extends JpaRepository<AssociationRules, Long>{
    List<AssociationRules> findByPeriodLabel(String periodLabel);

    // 연관 상품 추천을 위한 함수
    @Query("""
        select ar.itemset_b
        from AssociationRules ar
        WHERE ar.itemset_a = :subName
        ORDER BY ar.confidence DESC
        """)
    List<String> findTopRelatedSubNames(@Param("subName") String subName);
}

