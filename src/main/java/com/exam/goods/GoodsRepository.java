package com.exam.goods;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Query("SELECT g.goods_stock from Goods g where g.goods_id = :goodsId")
    Optional<Long> findStockByGoodsId(@Param("goodsId") Long goodsId);

    // 할인
    List<Goods> findAllByDiscountEndAtBefore(LocalDateTime time);

    // 연관 상품 가져오기 (랜덤으로)
    @Query("""
    SELECT g 
    FROM Goods g 
    WHERE g.subCategory.sub_name = :subName
    ORDER BY FUNCTION('RAND')
    """)
    List<Goods> findRandomGoodsBySubName(@Param("subName") String subName,Pageable pageable);


    // id로 sub_name 찾기
    @Query("""
        SELECT sc.sub_name
        FROM Goods g
        JOIN SubCategory sc ON g.subCategory.sub_category_id = sc.sub_category_id
        WHERE g.goods_id = :goodsId
        """)
    String findSubNameByGoodsId(@Param("goodsId") Long goodsId);
}

