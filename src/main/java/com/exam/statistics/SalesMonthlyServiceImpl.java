package com.exam.statistics;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesMonthlyServiceImpl implements SalesMonthlyService{

    SalesMonthlyRepository salesMonthlyRepository;

    public SalesMonthlyServiceImpl(SalesMonthlyRepository salesMonthlyRepository) {
        this.salesMonthlyRepository = salesMonthlyRepository;
    }

    @Override
    public SalesMonthlyDTO findBySalesMonth(String salesMonth) {

        SalesMonthly monthly = salesMonthlyRepository.findBySalesMonth(salesMonth);

        SalesMonthlyDTO dto = SalesMonthlyDTO.builder()
                    .saleMonth(monthly.getSaleMonth())
                    .totalSales(monthly.getTotalSales())
                    .totalOrders(monthly.getTotalOrders())
                    .build();

        return dto;
    }

    @Override
    public List<SalesMonthlyDTO> findBySalesYear(String year) {

        List<SalesMonthly> monthlyList = salesMonthlyRepository.findBySalesYear(year);

        List<SalesMonthlyDTO> monthlyDTO = monthlyList.stream().map(s -> {
            SalesMonthlyDTO dto = SalesMonthlyDTO.builder()
                    .saleMonth(s.getSaleMonth())
                    .totalSales(s.getTotalSales())
                    .totalOrders(s.getTotalOrders())
                    .build();
            return dto;
        }).collect(Collectors.toList());

        return monthlyDTO;
    }
}
