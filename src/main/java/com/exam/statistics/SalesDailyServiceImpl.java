package com.exam.statistics;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
                    .salesCategory(s.dailyCompositeKey.getSalesCategory())
                    .totalAmount(s.getTotalAmount())
                    .totalOrders(s.getTotalOrders())
                    .build();
            return dto;
        }).collect(Collectors.toList());
        return dailyDTO;
    }

}
