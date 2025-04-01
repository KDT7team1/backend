package com.exam.saleData;



import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SaleDataServiceImpl implements SaleDataService {


    private final SaleDataRepository saleDataRepository;

    public SaleDataServiceImpl(SaleDataRepository saleDataRepository) {
        this.saleDataRepository = saleDataRepository;
    }

    @Override
    public List<SalesChartDTO> getWeeklySalesByGoodsId(Long goodsId) {
        LocalDateTime today = LocalDate.now().atTime(23, 59, 59);
        LocalDateTime sevenDaysAgo = LocalDate.now().minusDays(6).atStartOfDay(); // 7일

        List<Object[]> rawResults = saleDataRepository.findWeeklySalesByGoodsId(goodsId, sevenDaysAgo, today);

        return rawResults.stream()
                .map(row -> new SalesChartDTO(((java.sql.Date) row[0]).toLocalDate(), ((Number) row[1]).intValue()))
                .collect(Collectors.toList());
    }
}
