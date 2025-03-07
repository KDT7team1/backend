package com.exam.userGoods.repository;


import com.exam.userGoods.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GoodsRepository extends JpaRepository<Goods, Long> {

    @Query("SELECT g FROM Goods g WHERE g.category.firstName = :firstName")
    List<Goods> findByFirstCategory(@Param("firstName") String firstName);

    @Query("SELECT g FROM Goods g WHERE g.category.firstName = :firstName and g.category.secondName = :secondName")
    List<Goods> findBySecondCategory(@Param("firstName") String firstName, @Param("secondName") String secondName);

    
}

