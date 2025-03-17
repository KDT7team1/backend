package com.exam.statistics;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        // 0 ~ 23시까지의 모든 시간대 배열
        List<Integer> allHours = IntStream.range(0, 24).boxed().collect(Collectors.toList());

        // 시간대별 매출 데이터 가져오기
        List<Object[]> dailyList = salesDailyRepository.getHourlySalesByDate(date);

        // salesHourly 데이터가 없으면 0으로 채워줌
        List<SalesDailyDTO> dailyDTO = allHours.stream().map(hour -> {
            // 해당 hour에 대한 데이터 찾기
            Object[] matchingRow = dailyList.stream().filter(row ->
                    ((Integer) row[0]).equals(hour)).findFirst().orElse(null);

            // 매칭되는 데이터가 없으면 0으로 채움
            if (matchingRow != null) {
                return SalesDailyDTO.builder()
                        .salesHour(hour)
                        .dailyPrice(((Number) matchingRow[1]).longValue())
                        .dailyAmount(((Number) matchingRow[2]).longValue())
                        .build();
            } else {
                return SalesDailyDTO.builder()
                        .salesHour(hour)
                        .dailyPrice(0L)
                        .dailyAmount(0L)
                        .build();
            }
        }).collect(Collectors.toList());

        return dailyDTO;
    }

}
