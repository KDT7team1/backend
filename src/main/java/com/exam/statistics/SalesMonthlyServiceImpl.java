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
    public List<SalesMonthlyDTO> findBySalesMonth(String salesMonth) {

        List<SalesMonthly> monthlyList = salesMonthlyRepository.findBySalesMonth(salesMonth);

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
