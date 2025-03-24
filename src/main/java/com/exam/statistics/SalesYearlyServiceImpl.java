package com.exam.statistics;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesYearlyServiceImpl implements SalesYearlyService{

    SalesYearlyRepository salesYearlyRepository;

    public SalesYearlyServiceImpl(SalesYearlyRepository salesYearlyRepository) {
        this.salesYearlyRepository = salesYearlyRepository;
    }

    @Override
    public List<SalesYearlyDTO> getCategorySalesByYear(String year) {

        List<Object[]> yearlyList = salesYearlyRepository.getCategorySalesByYear(year);

        List<SalesYearlyDTO> list = yearlyList.stream().map(s -> {
            SalesYearlyDTO dto = SalesYearlyDTO.builder()
                    .year(year)
                    .categoryId((Long) s[0])
                    .yearlyPrice((Long) s[1])
                    .yearlyAmount((Long) s[2])
                    .build();
            return dto;
        }).collect(Collectors.toList());

        return list;
    }

    @Override
    public List<SalesYearlyDTO> getSubCategorySalesByYear(String year, Long categoryId) {
        List<Object[]> yearlyList = salesYearlyRepository.getSubCategorySalesByYear(year, categoryId);

        List<SalesYearlyDTO> list = yearlyList.stream().map(s -> {
            SalesYearlyDTO dto = SalesYearlyDTO.builder()
                    .year(year)
                    .categoryId((Long) s[0])
                    .subCategoryId((Long) s[1])
                    .yearlyPrice((Long) s[2])
                    .yearlyAmount((Long) s[3])
                    .build();
            return dto;
        }).collect(Collectors.toList());

        return list;
    }
}
