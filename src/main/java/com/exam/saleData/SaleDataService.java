package com.exam.saleData;


import java.util.List;

public interface SaleDataService {
    List<SalesChartDTO> getWeeklySalesByGoodsId(Long goodsId);
}
