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
                    .salesMonth(s.monthlyCompositeKey.getSalesMonth())
                    .categoryId(s.monthlyCompositeKey.getCategoryId())
                    .subCategoryId(s.monthlyCompositeKey.getSubCategoryId())
                    .monthlyPrice(s.getMonthlyPrice())
                    .monthlyAmount(s.getMonthlyAmount())
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
                    .salesMonth(s.monthlyCompositeKey.getSalesMonth())
                    .categoryId(s.monthlyCompositeKey.getCategoryId())
                    .subCategoryId(s.monthlyCompositeKey.getSubCategoryId())
                    .monthlyPrice(s.getMonthlyPrice())
                    .monthlyAmount(s.getMonthlyAmount())
                    .build();
            return dto;
        }).collect(Collectors.toList());

        return monthlyDTO;
    }

    @Override
    public List<SalesMonthlyDTO> getMonthlySalesByYear(String year) {
        // 월별 매출 데이터 가져오기
        List<Object[]> yearlyList = salesMonthlyRepository.getMonthlySalesByYear(year);

        List<SalesMonthlyDTO> monthlyList = yearlyList.stream().map(s -> {
            SalesMonthlyDTO dto = SalesMonthlyDTO.builder()
                    .salesMonth((String) s[0])
                    .monthlyPrice((Long) s[1])
                    .monthlyAmount((Long) s[2])
                    .build();
            return dto;
        }).collect(Collectors.toList());

        return monthlyList;
    }
}
