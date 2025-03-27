package com.exam.salesAnalysis;

import com.exam.cartAnalysis.repository.SaleDataRepository;
import com.exam.salesAlert.SalesAlertDTO;
import com.exam.salesAlert.SalesAlertRepository;
import com.exam.statistics.SalesDaily;
import com.exam.statistics.SalesDailyDTO;
import com.exam.statistics.SalesDailyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SalesAnalysisServiceImpl implements SalesAnalysisService {

    SalesDailyRepository dailyRepository;
    SaleDataRepository dataRepository;
    SalesAlertRepository alertRepository;

    public SalesAnalysisServiceImpl(SalesDailyRepository dailyRepository, SaleDataRepository dataRepository, SalesAlertRepository alertRepository) {
        this.dailyRepository = dailyRepository;
        this.dataRepository = dataRepository;
        this.alertRepository = alertRepository;
    }

    @Override
    public List<SalesDailyDTO> getSalesDailyByDateAndHour(LocalDate salesDate, int salesHour) {
        // 특정 날짜와 시간대의 매출 데이터 조회
        List<SalesDaily> entityList = dailyRepository.findBySalesDateAndSalesHour(salesDate, salesHour);

        List<SalesDailyDTO> dtoList = entityList.stream().map(s -> {
            SalesDailyDTO dto = SalesDailyDTO.builder()
                    .salesDate(s.dailyCompositeKey.getSalesDate())
                    .salesHour(s.dailyCompositeKey.getSalesHour())
                    .subCategoryId(s.dailyCompositeKey.getSubCategoryId())
                    .categoryId(s.dailyCompositeKey.getCategoryId())
                    .dailyPrice(s.getDailyPrice())
                    .dailyAmount(s.getDailyAmount())
                    .build();
            return dto;
        }).collect(Collectors.toList());

        return dtoList;
    }

    @Override
    public Map<String, Object> getAverageSalesLast7Days(LocalDate salesDate, int salesHour) {
        // 지난 7일간의 시간대별 판매량 데이터와 평균 매출액 반환
        LocalDate startDate = salesDate.minusDays(8);
        LocalDate endDate = salesDate.minusDays(1);

        List<SalesDaily> entityList = dailyRepository.findBySalesDateBetweenAndSalesHour(startDate, endDate, salesHour);

        // 평균값 구하기
        Long totalPrice = 0L;
        Long totalAmount = 0L;
        int count = 0;

        List<SalesDailyDTO> dtoList = new ArrayList<>();

        // 반복문 속에서 entity를 DTO로 변환 + 평균값 계산
        for (SalesDaily s : entityList) {
            dtoList.add(SalesDailyDTO.builder()
                    .salesDate(s.dailyCompositeKey.getSalesDate())
                    .salesHour(s.dailyCompositeKey.getSalesHour())
                    .subCategoryId(s.dailyCompositeKey.getSubCategoryId())
                    .categoryId(s.dailyCompositeKey.getCategoryId())
                    .dailyPrice(s.getDailyPrice())
                    .dailyAmount(s.getDailyAmount())
                    .build());

            totalPrice += s.getDailyPrice();
            totalAmount += s.getDailyAmount();
            count++;
        }

        long averagePrice = count > 0 ? Math.round((double) totalPrice / count) : 0;
        long averageAmount = count > 0 ? Math.round((double) totalAmount / count ) : 0;

        // Map에 넣고 반환
        Map<String, Object> result = new HashMap<>();
        result.put("salesList", dtoList); // 매출 데이터 리스트
        result.put("averagePrice", averagePrice); // 평균 매출액
        result.put("averageAmount", averageAmount); // 평균 주문량

        return result;
    }

    @Override
    public Map<String, Object> getAverageSalesLast30Days(LocalDate salesDate, int salesHour) {
        // 지난 30일간의 시간대별 판매량 데이터와 평균 매출액 반환
        LocalDate startDate = salesDate.minusDays(31);
        LocalDate endDate = salesDate.minusDays(1);

        List<SalesDaily> entityList = dailyRepository.findBySalesDateBetweenAndSalesHour(startDate, endDate, salesHour);

        // 평균값 구하기
        Long totalPrice = 0L;
        Long totalAmount = 0L;
        int count = 0;

        List<SalesDailyDTO> dtoList = new ArrayList<>();

        // 반복문 속에서 entity를 DTO로 변환 + 평균값 계산
        for (SalesDaily s : entityList) {
            dtoList.add(SalesDailyDTO.builder()
                    .salesDate(s.dailyCompositeKey.getSalesDate())
                    .salesHour(s.dailyCompositeKey.getSalesHour())
                    .subCategoryId(s.dailyCompositeKey.getSubCategoryId())
                    .categoryId(s.dailyCompositeKey.getCategoryId())
                    .dailyPrice(s.getDailyPrice())
                    .dailyAmount(s.getDailyAmount())
                    .build());

            totalPrice += s.getDailyPrice();
            totalAmount += s.getDailyAmount();
            count++;
        }

        long averagePrice = count > 0 ? Math.round((double) totalPrice / count) : 0;
        long averageAmount = count > 0 ? Math.round((double) totalAmount / count ) : 0;

        // Map에 넣고 반환
        Map<String, Object> result = new HashMap<>();
        result.put("salesList", dtoList); // 매출 데이터 리스트
        result.put("averagePrice", averagePrice); // 평균 매출액
        result.put("averageAmount", averageAmount); // 평균 주문량

        return result;
    }

    @Override
    public SalesAlertDTO detectSalesAnomaly(LocalDate targetDate, int targetHour) {
        List<SalesDailyDTO> todaySalesList = getSalesDailyByDateAndHour(targetDate, targetHour);
        if (todaySalesList.isEmpty()) {
            return null; // 오늘의 데이터가 없으면 처리하지 않음
        }

        // 이전 시간대의 총 매출액 계산
        long todaySales = todaySalesList.stream().mapToLong(SalesDailyDTO::getDailyPrice).sum();

        // 7일 전 같은 시간대의 매출액
        LocalDate aWeekAgo = targetDate.minusDays(7);
        List<SalesDailyDTO> aWeekAgoSalesList = getSalesDailyByDateAndHour(aWeekAgo, targetHour);
        long aWeekAgoSales = aWeekAgoSalesList.stream().mapToLong(SalesDailyDTO::getDailyPrice).sum();

        // 7일간의 평균 매출액 계산 - 단기 트렌드 분석
        Map<String, Object> avg7DaysData = getAverageSalesLast7Days(targetDate, targetHour);
        long avg7DaysSales = (long) avg7DaysData.get("averagePrice");

        // 30일간의 평균 매출액 계산 - 장기 트렌드 분석
        Map<String, Object> avg30DaysData = getAverageSalesLast30Days(targetDate, targetHour);
        long avg30DaysSales = (long) avg30DaysData.get("averagePrice");

        // 매출 차이 계산
        long diffAWeekAgo = todaySales - aWeekAgoSales;
        long diffAvg7Days = todaySales - avg7DaysSales;
        long diffAvg30Days = todaySales - avg30DaysSales;

        // 비율 차이 계산
        double percentDiffAWeekAgo = (aWeekAgoSales > 0) ? (diffAWeekAgo * 100.0 / aWeekAgoSales) : 0;
        double percentDiffAvg7Days = (avg7DaysSales > 0) ? (diffAvg7Days * 100.0 / avg7DaysSales) : 0;
        double percentDiffAvg30Days = (avg30DaysSales > 0) ? (diffAvg30Days * 100.0 / avg30DaysSales) : 0;

        // 1주일 전과의 비교
        if (Math.abs(percentDiffAWeekAgo) >= 10) {
            String alertMessage = generateAlertMessage(1, percentDiffAWeekAgo, todaySales, aWeekAgoSales);
            return createSalesAlertDTO(targetDate, aWeekAgoSales, todaySales, diffAWeekAgo, alertMessage);
        }

        // 7일 평균 대비 비교
        if (Math.abs(percentDiffAvg7Days) >= 10) {
            String alertMessage = generateAlertMessage(7, percentDiffAvg7Days, todaySales, avg7DaysSales);
            return createSalesAlertDTO(targetDate, avg7DaysSales, todaySales, diffAvg7Days, alertMessage);
        }

        // 30일 평균 대비 비교
        if (Math.abs(percentDiffAvg30Days) >= 10) {
            String alertMessage = generateAlertMessage(30, percentDiffAvg30Days, todaySales, avg30DaysSales);
            return createSalesAlertDTO(targetDate, avg30DaysSales, todaySales, diffAvg30Days, alertMessage);
        }

        return null;
    }

    private SalesAlertDTO createSalesAlertDTO(LocalDate targetDate, long previousSales, long currentSales, long diffPrice, String alertMessage) {
        return SalesAlertDTO.builder()
                .alertDate(targetDate)
                .previousSales(previousSales)
                .currentSales(currentSales)
                .difference(diffPrice)
                .alertMessage(alertMessage)
                .build();
    }

    private String generateAlertMessage(int trendBasis, double percentDiffAWeekAgo, long todaySales, long aWeekAgoSales) {
        String trend = (percentDiffAWeekAgo > 0) ? "상승" : "하락";
        String trendPeriod = switch (trendBasis) {
            case 1 -> "1주일 전 같은 요일 대비";
            case 7 -> "[단기 트렌드] 7일 평균 대비";
            case 30 -> "[장기 트렌드] 30일 평균 대비";
            default -> throw new IllegalStateException("Unexpected value: " + trendBasis); // 예외 처리
        };

        // 메세지 포매팅
        // 예시) [단기 트렌드] 7일 평균 대비 30.7% 하락 : 오늘 매출: 53,000원, 비교 매출: 20,000원
        return String.format("\\uD83D\\uDCC5 **%s** %.1f%% %s : \\uD83D\\uDCCA **오늘 매출**: %,d원, \\uD83D\\uDCC9 **비교 매출**: %,d원",
                trendPeriod, percentDiffAWeekAgo, trend, todaySales, aWeekAgoSales);
    }

}
