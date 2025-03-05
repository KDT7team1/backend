package com.exam.statistics;

import java.util.List;
import java.util.stream.Collectors;

public class SalesDailyServiceImpl implements SalesDailyService{

    SalesDailyRepository salesDailyRepository;

    public SalesDailyServiceImpl(SalesDailyRepository salesDailyRepository) {
        this.salesDailyRepository = salesDailyRepository;
    }

    @Override
    public List<SalesDailyDTO> findBySalesDate(String date) {
        List<SalesDaily> dailyList = salesDailyRepository.findBySalesDate(date);

        List<SalesDailyDTO> dailyDTO = dailyList.stream().map(s -> {
            SalesDailyDTO dto = SalesDailyDTO.builder()
                    .totalAmount(s.totalAmount)
                    .totalOrders(s.totalOrders)
                    .build();
            return dto;
        }).collect(Collectors.toList());
        return List.of();
    }

    @Override
    public List<SalesDailyDTO> findMonthlySales(String date) {
        return List.of();
    }

}
