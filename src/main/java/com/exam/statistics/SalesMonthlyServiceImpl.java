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

        List<SalesMonthly> monthly = salesMonthlyRepository.findBySalesMonth(salesMonth);

        List<SalesMonthlyDTO> monthlyDTO = monthly.stream().map(s -> {
            SalesMonthlyDTO dto = SalesMonthlyDTO.builder()
                    .saleMonth(s.monthlyCompositeKey.getSaleMonth())
                    .salesCategory(s.monthlyCompositeKey.getSalesCategory())
                    .monthlyAmount(s.getMonthlyAmount())
                    .monthlyOrders(s.getMonthlyOrders())
                    .build();
            return dto;
        }).collect(Collectors.toList());

        return monthlyDTO;
    }

    @Override
    public List<SalesMonthlyDTO> findBySalesYear(String year) {

        List<SalesMonthly> monthlyList = salesMonthlyRepository.findBySalesYear(year);

        List<SalesMonthlyDTO> monthlyDTO = monthlyList.stream().map(s -> {
            SalesMonthlyDTO dto = SalesMonthlyDTO.builder()
                    .saleMonth(s.monthlyCompositeKey.getSaleMonth())
                    .salesCategory(s.monthlyCompositeKey.getSalesCategory())
                    .monthlyAmount(s.getMonthlyAmount())
                    .monthlyOrders(s.getMonthlyOrders())
                    .build();
            return dto;
        }).collect(Collectors.toList());

        return monthlyDTO;
    }
}
