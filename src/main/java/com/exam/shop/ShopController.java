package com.exam.shop;

import com.exam.goods.GoodsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
            @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(page, size);
        Page<GoodsDTO> goodsPage = shopService.getGoods(category, search, pageable);

        return ResponseEntity.ok(goodsPage);
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
}
