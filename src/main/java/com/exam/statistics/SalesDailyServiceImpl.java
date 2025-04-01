package com.exam.statistics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
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

    // 장바구니 분석에서 상품 7일치 판매기록 조회
    @Override
    public List<SalesChartDTO> getWeeklySales(Long categoryId, Long subCategoryId) {

        LocalDate today = LocalDate.now();
        LocalDate sevenDaysAgo = today.minusDays(6); // 총 7일치

        List<Object[]> rawResults = salesDailyRepository.findWeeklySalesByCategory(categoryId, subCategoryId, sevenDaysAgo, today);

        return rawResults.stream()
                .map(row -> new SalesChartDTO((LocalDate) row[0], ((Number) row[1]).intValue()))
                .collect(Collectors.toList());

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
    public List<SalesDailyDTO> getAvgHourlySalesByDate(LocalDate startDate, LocalDate endDate) {
        // 선택한 날짜 사이의 시간별 매출 평균 값
        List<Object[]> entityList = salesDailyRepository.getAvgHourlySalesByDate(startDate, endDate);

        List<SalesDailyDTO> dtoList = entityList.stream().map(s -> {
            SalesDailyDTO dto = SalesDailyDTO.builder()
                    .salesHour(((Number) s[0]).intValue())
                    .dailyPrice(((Number) s[1]).longValue())
                    .dailyAmount(((Number) s[2]).longValue())
                    .build();
            return dto;
        }).collect(Collectors.toList());

        return dtoList;
    }

    @Override
    public List<SalesDailyDTO> getAvgCategorySalesByDate(LocalDate startDate, LocalDate endDate) {
        // 선택한 날짜 사이의 카테고리별 매출 평균 값
        List<Object[]> entityList = salesDailyRepository.getAvgCategorySalesByDate(startDate, endDate);

        List<SalesDailyDTO> dtoList = entityList.stream().map(s -> {
            SalesDailyDTO dto = SalesDailyDTO.builder()
                    .categoryId(((Number) s[0]).longValue())
                    .subCategoryId(((Number) s[1]).longValue())
                    .dailyPrice(((Number) s[2]).longValue())
                    .dailyAmount(((Number) s[3]).longValue())
                    .build();
            return dto;
        }).collect(Collectors.toList());

        return dtoList;
    }

    @Override
    public List<SalesDailyDTO> getTotalHourlySalesByDate(LocalDate startDate, LocalDate endDate) {
        List<Object[]> entityList = salesDailyRepository.getTotalHourlySalesByDate(startDate, endDate);

        List<SalesDailyDTO> dtoList = entityList.stream().map(s -> {
            SalesDailyDTO dto = SalesDailyDTO.builder()
                    .salesDate((LocalDate) s[0])
                    .salesHour(((Number) s[1]).intValue())
                    .dailyPrice(((Number) s[2]).longValue())
                    .dailyAmount(((Number) s[3]).longValue())
                    .build();
            return dto;
        }).collect(Collectors.toList());

        return dtoList;
    }

    @Override
    public List<SalesDailyDTO> getTotalCategorySalesByDate(LocalDate startDate, LocalDate endDate) {
        List<Object[]> entityList = salesDailyRepository.getTotalCategorySalesByDate(startDate, endDate);

        List<SalesDailyDTO> dtoList = entityList.stream().map(s -> {
            SalesDailyDTO dto = SalesDailyDTO.builder()
                    .salesDate((LocalDate) s[0])
                    .categoryId(((Number) s[1]).longValue())
                    .subCategoryId(((Number) s[2]).longValue())
                    .dailyPrice(((Number) s[3]).longValue())
                    .dailyAmount(((Number) s[4]).longValue())
                    .build();
            return dto;
        }).collect(Collectors.toList());

        return dtoList;
    }

}
