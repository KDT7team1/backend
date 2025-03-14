package com.exam.statistics;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesDailyServiceImpl implements SalesDailyService{

    SalesDailyRepository salesDailyRepository;

    public SalesDailyServiceImpl(SalesDailyRepository salesDailyRepository) {
        this.salesDailyRepository = salesDailyRepository;
    }

    @Override
    public List<SalesDailyDTO> findBySalesDate(LocalDate searchDate) {
        List<SalesDaily> dailyList = salesDailyRepository.findBySalesDate(searchDate);

        List<SalesDailyDTO> dailyDTO = dailyList.stream().map(s -> {
            SalesDailyDTO dto = SalesDailyDTO.builder()
                    .salesDate(s.dailyCompositeKey.getSalesDate())
                    .salesHour(s.dailyCompositeKey.getSalesHour())
                    .categoryId(s.dailyCompositeKey.getSubCategoryId())
                    .dailyPrice(s.getDailyPrice())
                    .dailyAmount(s.getDailyAmount())
                    .build();
            return dto;
        }).collect(Collectors.toList());
        return dailyDTO;
    }

    @Override
    public List<SalesDailyDTO> getHourlySalesByDate(LocalDate date) {
        List<SalesDaily> dailyList = salesDailyRepository.getHourlySalesByDate(date);

        List<SalesDailyDTO> dailyDTO = dailyList.stream().map(s -> {
            SalesDailyDTO dto = SalesDailyDTO.builder()
                    .salesDate(s.dailyCompositeKey.getSalesDate())
                    .salesHour(s.dailyCompositeKey.getSalesHour())
                    .categoryId(s.dailyCompositeKey.getSubCategoryId())
                    .dailyPrice(s.getDailyPrice())
                    .dailyAmount(s.getDailyAmount())
                    .build();
            return dto;
        }).collect(Collectors.toList());
        return dailyDTO;
    }

}
