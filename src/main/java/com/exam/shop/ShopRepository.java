package com.exam.shop;

import com.exam.goods.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Goods, Long> {

    // 카테고리 & 검색 필터링 추가
    @Query("SELECT g FROM Goods g " +
            "WHERE (:category IS NULL OR g.category.category_id = :category) " +
            "AND (:search IS NULL OR g.goods_name LIKE %:search% OR g.goods_description LIKE %:search%)")
    Page<Goods> findAllByFilters(@Param("category") Long category,
                                 @Param("search") String search,
                                 Pageable pageable);

    // 재고가 있는 모든 상품 조회
    @Query("SELECT DISTINCT g FROM Goods g " +
            "JOIN g.inventoryList i " +
            "WHERE i.stockQuantity > 0")
    List<Goods> findAllInStockGoods();

    // 할인 중인 상품 리스트
    @Query("""
            SELECT DISTINCT g FROM Goods g
            WHERE g.discountRate > 0
            AND g.discountEndAt > :currentDate
            """)
    List<Goods> findDiscountedGoodsInStock(@Param("currentDate") LocalDateTime currentDate);


}
