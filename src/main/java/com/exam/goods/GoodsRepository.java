package com.exam.goods;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long> {

    @Query("SELECT g FROM Goods g WHERE g.category.firstName = :firstName")
    List<Goods> findByFirstCategory(@Param("firstName") String firstName);

    @Query("SELECT g FROM Goods g WHERE g.category.firstName = :firstName and g.category.secondName = :secondName")
    List<Goods> findBySecondCategory(@Param("firstName") String firstName, @Param("secondName") String secondName);

    @Query("SELECT g FROM Goods g WHERE g.category.category_id = :categoryId")
    List<Goods> findByCategoryId(@Param("categoryId") long categoryId);

    @Query("SELECT g FROM Goods g WHERE g.subCategory.sub_category_id = :subCategoryId")
    List<Goods> findBySubCategoryId(@Param("subCategoryId") long subCategoryId);

}

