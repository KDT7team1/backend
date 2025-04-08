package com.exam.saleData;


import com.exam.salesHistory.ReceiptDTO;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SaleDataService {
    List<SalesChartDTO> getWeeklySalesByGoodsId(Long goodsId);
    ReceiptDTO getReceiptByOrdersId(Long ordersId);
}
