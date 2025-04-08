package com.exam.shop;

import com.exam.goods.GoodsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/shop")
public class ShopController {

    ShopService shopService;

    public ShopController(ShopService shopService) {

        this.shopService = shopService;
    }

    // 모든 상품 조회 - 페이지네이션(12)
    @GetMapping("/getGoods")
    public ResponseEntity<Page<GoodsDTO>> getGoods(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long minPrice,  // 최소 가격
            @RequestParam(required = false) Long maxPrice,  // 최대 가격
            @RequestParam(required = false) String sort   // 정렬 기준
            ) {

        Pageable pageable = getPageable(page, size, sort);
        Page<GoodsDTO> goodsPage = shopService.getGoods(category, search, minPrice, maxPrice, pageable);

        return ResponseEntity.ok(goodsPage);
    }

    // 필터링을 위한 getPageable 함수 추가
    private Pageable getPageable(int page, int size, String sortBy) {
        if (sortBy == null || sortBy.isEmpty()) {
            return PageRequest.of(page, size); // 기본 정렬 (기본적으로 상품 ID나 이름 기준)
        }

        // 정렬 기준에 따라 정렬 적용
        return switch (sortBy) {
            case "price-low" -> PageRequest.of(page, size, Sort.by("g.goods_price").ascending());
            case "price-high" -> PageRequest.of(page, size, Sort.by("g.goods_price").descending());
            case "name" -> PageRequest.of(page, size, Sort.by("g.goods_name").ascending());
            case "popularity" -> PageRequest.of(page, size, Sort.by("g.goods_orders").descending());
            default -> PageRequest.of(page, size);  // 기본 정렬
        };
    }

    // 재고가 있는 모든 상품 조회
    @GetMapping("/findAllInStockGoods")
    public ResponseEntity<List<GoodsDTO>> findAllInStockGoods() {
        List<GoodsDTO> list = shopService.findAllInStockGoods();
        return ResponseEntity.status(200).body(list);
    }

    // 할인 중인 모든 상품 조회
    @GetMapping("/isDiscountedList")
    public ResponseEntity<List<GoodsDTO>> getIsDiscountedList() {
        List<GoodsDTO> list = shopService.getIsDiscountedList();
        return ResponseEntity.status(200).body(list);
    }

    // 최근 1주일간 판매량 top 10 상품 조회
    @GetMapping("/top10")
    public ResponseEntity<List<Map<String, Object>>> findTop10GoodsForAWeek() {
        LocalDate today = LocalDate.now();
        List<Map<String, Object>> list = shopService.findTop10GoodsForAWeek(today);
        return ResponseEntity.status(200).body(list);
    }
}
