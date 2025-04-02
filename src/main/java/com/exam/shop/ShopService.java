package com.exam.shop;

import com.exam.goods.GoodsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShopService {

    // 모든 상품 조회(페이지네이션)
    public Page<GoodsDTO> getGoods(Long category, String search, Pageable pageable);

    // 재고가 있는 모든 상품 조회
    List<GoodsDTO> findAllInStockGoods();

    // 할인 중인 상품 조회
    List<GoodsDTO> getIsDiscountedList();
}
