package com.exam.shop;

import com.exam.goods.GoodsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ShopService {

    // 모든 상품 조회(페이지네이션)
    public Page<GoodsDTO> getGoods(Long category, String search, Long minPrice, Long maxPrice, Pageable pageable);

    // 재고가 있는 모든 상품 조회
    List<GoodsDTO> findAllInStockGoods();

    // 할인 중인 상품 조회
    List<GoodsDTO> getIsDiscountedList();

    // 최근 7일간 판매횟수 top 10 아이템
    List<Map<String, Object>> findTop10GoodsForAWeek(LocalDate targetDate);

}
