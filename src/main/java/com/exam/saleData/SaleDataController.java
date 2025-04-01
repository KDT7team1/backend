package com.exam.saleData;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/saleData")
public class SaleDataController {

    private final SaleDataService saleDataService;

    public SaleDataController(SaleDataService saleDataService) {
        this.saleDataService = saleDataService;
    }

    @GetMapping("/week/{goodsId}")
    public ResponseEntity<List<SalesChartDTO>>
    getWeeklySalesByGoodsId(@PathVariable Long goodsId) {
        List<SalesChartDTO> list = saleDataService.getWeeklySalesByGoodsId(goodsId);
        return ResponseEntity.status(200).body(list);
    }




}
