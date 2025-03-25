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
                        .salesDate(date)
                        .salesHour(hour)
                        .dailyPrice(((Number) matchingRow[1]).longValue())
                        .dailyAmount(((Number) matchingRow[2]).longValue())
                        .build();
            } else {
                return SalesDailyDTO.builder()
                        .salesDate(date)
                        .salesHour(hour)
                        .dailyPrice(0L)
                        .dailyAmount(0L)
                        .build();
            }
        }).collect(Collectors.toList());

        return dailyDTO;
    }

    @Override
    public List<SalesDailyDTO> getDailySalesByMonth(String month) {
        // 선택한 월의 일별 매출 데이터 가져오기
        List<Object[]> monthlyList = salesDailyRepository.getDailySalesByMonth(month);

        List<SalesDailyDTO> dailyList = monthlyList.stream().map(d -> {
            SalesDailyDTO dto = SalesDailyDTO.builder()
                    .salesDate((LocalDate)d[0])
                    .dailyPrice((Long)d[1])
                    .dailyAmount((Long)d[2])
                    .build();
            return dto;
        }).collect(Collectors.toList());

        return dailyList;
    }

    @Override
    public List<SalesDailyDTO> getCategorySalesByDate(LocalDate date) {
        // 해당하는 날짜의 카테고리별 매출 데이터 - 대분류
        List<Object[]> categoryList = salesDailyRepository.getCategorySalesByDate(date);

        List<SalesDailyDTO> list = categoryList.stream().map(c -> {
            SalesDailyDTO dto = SalesDailyDTO.builder()
                    .salesDate(date)
                    .categoryId((Long)c[0])
                    .dailyPrice((Long)c[1])
                    .dailyAmount((Long)c[2])
                    .build();
            return dto;
        }).collect(Collectors.toList());

        return list;
    }

    @Override
    public List<SalesDailyDTO> getSubCategorySalesByDate(LocalDate date, Long categoryId) {
        // 해당하는 날짜와 대분류의 소분류별 매출 데이터
        List<Object[]> categoryList = salesDailyRepository.getSubCategorySalesByDate(date, categoryId);

        List<SalesDailyDTO> list = categoryList.stream().map(c -> {
            SalesDailyDTO dto = SalesDailyDTO.builder()
                    .salesDate(date)
                    .categoryId((Long) c[0])
                    .subCategoryId((Long) c[1])
                    .dailyPrice((Long) c[2])
                    .dailyAmount((Long) c[3])
                    .build();
            return dto;
        }).collect(Collectors.toList());

        return list;
    }

    @Override
    public SalesDailyDTO getAvgHourlySalesByDate(LocalDate startDate, LocalDate endDate) {
        // 선택한 날짜 사이의 시간별 매출 평균 값
        Object[] entityList = salesDailyRepository.getAvgHourlySalesByDate(startDate, endDate);

        SalesDailyDTO dto = SalesDailyDTO.builder()
                .salesHour((int) entityList[0])
                .dailyPrice((Long) entityList[1])
                .dailyAmount((Long) entityList[2])
                .build();

        return dto;
    }

    @Override
    public SalesDailyDTO getAvgCategorySalesByDate(LocalDate startDate, LocalDate endDate) {
        // 선택한 날짜 사이의 카테고리별 매출 평균 값
        Object[] entityList = salesDailyRepository.getAvgCategorySalesByDate(startDate, endDate);

        SalesDailyDTO dto = SalesDailyDTO.builder()
                .categoryId((Long) entityList[0])
                .subCategoryId((Long) entityList[1])
                .dailyPrice((Long) entityList[2])
                .dailyAmount((Long) entityList[3])
                .build();

        return dto;
    }

}
