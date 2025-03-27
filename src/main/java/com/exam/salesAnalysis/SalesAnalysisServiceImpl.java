package com.exam.salesAnalysis;

import com.exam.cartAnalysis.repository.SaleDataRepository;
import com.exam.salesAlert.SalesAlertDTO;
import com.exam.salesAlert.SalesAlertRepository;
import com.exam.salesAlert.SalesAlertService;
import com.exam.statistics.SalesDaily;
import com.exam.statistics.SalesDailyDTO;
import com.exam.statistics.SalesDailyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesAnalysisServiceImpl implements SalesAnalysisService {

    SalesDailyRepository dailyRepository;
    SaleDataRepository dataRepository;
    SalesAlertRepository alertRepository;
    SalesAlertService alertService;

    public SalesAnalysisServiceImpl(SalesDailyRepository dailyRepository, SaleDataRepository dataRepository, SalesAlertRepository alertRepository, SalesAlertService alertService) {
        this.dailyRepository = dailyRepository;
        this.dataRepository = dataRepository;
        this.alertRepository = alertRepository;
        this.alertService = alertService;
    }

    @Override
    public List<SalesDailyDTO> getSalesDailyByDateAndHour(LocalDate salesDate, int salesHour) {
        // 특정 날짜와 시간대의 매출 데이터 조회
        List<SalesDaily> entityList = dailyRepository.findBySalesDateAndSalesHour(salesDate, salesHour);

        return entityList.stream().map(s -> {
            return SalesDailyDTO.builder()
                    .salesDate(s.dailyCompositeKey.getSalesDate())
                    .salesHour(s.dailyCompositeKey.getSalesHour())
                    .subCategoryId(s.dailyCompositeKey.getSubCategoryId())
                    .categoryId(s.dailyCompositeKey.getCategoryId())
                    .dailyPrice(s.getDailyPrice())
                    .dailyAmount(s.getDailyAmount())
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public long getAverageSalesLast7Days(LocalDate salesDate, int salesHour) {
        // 지난 7일간의 시간대별 판매량 데이터와 평균 매출액 반환
        LocalDate startDate = salesDate.minusDays(8);
        LocalDate endDate = salesDate.minusDays(1);

        List<SalesDaily> entityList = dailyRepository.findBySalesDateBetweenAndSalesHour(startDate, endDate, salesHour);

        // 평균값 구하기
        Long totalPrice = 0L;
        int count = 0;

        // 반복문 속에서 entity를 DTO로 변환 + 평균값 계산
        for (SalesDaily s : entityList) {
            totalPrice += s.getDailyPrice();
            count++;
        }

        return count > 0 ? Math.round((double) totalPrice / count) : 0L;
    }

    @Override
    public long getAverageSalesLast30Days(LocalDate salesDate, int salesHour) {
        // 지난 30일간의 시간대별 평균 매출액 반환
        LocalDate startDate = salesDate.minusDays(31);
        LocalDate endDate = salesDate.minusDays(1);

        List<SalesDaily> entityList = dailyRepository.findBySalesDateBetweenAndSalesHour(startDate, endDate, salesHour);

        // 평균값 구하기
        Long totalPrice = 0L;
        int count = 0;

        // 반복문 속에서 entity를 DTO로 변환 + 평균값 계산
        for (SalesDaily s : entityList) {
            totalPrice += s.getDailyPrice();
            count++;
        }

        return count > 0 ? Math.round((double) totalPrice / count) : 0L;
    }

    @Override
    public void detectSalesAnomaly(LocalDate targetDate, int targetHour) {
        List<SalesDailyDTO> todaySalesList = getSalesDailyByDateAndHour(targetDate, targetHour);
        if (todaySalesList.isEmpty()) {
            return; // 오늘의 데이터가 없으면 처리하지 않음
        }

        // 이전 시간대의 총 매출액 계산
        long todaySales = todaySalesList.stream().mapToLong(SalesDailyDTO::getDailyPrice).sum();

        // 7일 전 같은 시간대의 매출액
        LocalDate aWeekAgo = targetDate.minusDays(7);
        List<SalesDailyDTO> aWeekAgoSalesList = getSalesDailyByDateAndHour(aWeekAgo, targetHour);
        long aWeekAgoSales = aWeekAgoSalesList.stream().mapToLong(SalesDailyDTO::getDailyPrice).sum();

        // 7일간의 평균 매출액 계산 - 단기 트렌드 분석
        long avg7DaysSales = getAverageSalesLast7Days(targetDate, targetHour);

        // 30일간의 평균 매출액 계산 - 장기 트렌드 분석
        long avg30DaysSales = getAverageSalesLast30Days(targetDate, targetHour);

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
            String alertMessage = generateAlertMessage(1, targetHour, percentDiffAWeekAgo, todaySales, aWeekAgoSales);
            SalesAlertDTO alertDTO = createSalesAlertDTO(1, targetDate, targetHour, aWeekAgoSales, todaySales, diffAWeekAgo, alertMessage);
            alertService.save(alertDTO);// 바로 DB에 저장
        }

        // 7일 평균 대비 비교
        if (Math.abs(percentDiffAvg7Days) >= 10) {
            String alertMessage = generateAlertMessage(7, targetHour, percentDiffAvg7Days, todaySales, avg7DaysSales);
            SalesAlertDTO alertDTO = createSalesAlertDTO(7, targetDate, targetHour, avg7DaysSales, todaySales, diffAvg7Days, alertMessage);
            alertService.save(alertDTO);// 바로 DB에 저장
        }

        // 30일 평균 대비 비교
        if (Math.abs(percentDiffAvg30Days) >= 10) {
            String alertMessage = generateAlertMessage(30, targetHour, percentDiffAvg30Days, todaySales, avg30DaysSales);
            SalesAlertDTO alertDTO = createSalesAlertDTO(30, targetDate, targetHour, avg30DaysSales, todaySales, diffAvg30Days, alertMessage);
            alertService.save(alertDTO);// 바로 DB에 저장
        }

    }

    private SalesAlertDTO createSalesAlertDTO(int trendBasis, LocalDate targetDate, int targetHour, long previousSales, long currentSales, long diffPrice, String alertMessage) {
        return SalesAlertDTO.builder()
                .trendBasis(trendBasis)
                .alertDate(targetDate)
                .alertHour(targetHour)
                .previousSales(previousSales)
                .currentSales(currentSales)
                .difference(diffPrice)
                .alertMessage(alertMessage)
                .build();
    }

    private String generateAlertMessage(int trendBasis, int targetHour, double percentDiffAWeekAgo, long todaySales, long aWeekAgoSales) {
        String trend = (percentDiffAWeekAgo > 0) ? "상승" : "하락";
        String trendPeriod = switch (trendBasis) {
            case 1 -> "[동요일] 일주일 전 같은 요일 대비";
            case 7 -> "[단기 트렌드] 7일 평균 대비";
            case 30 -> "[장기 트렌드] 30일 평균 대비";
            default -> throw new IllegalStateException("Unexpected value: " + trendBasis); // 예외 처리
        };

        // 메세지 포매팅
        // 예시) [14시] [단기 트렌드] 7일 평균 대비 30.7% 하락 : 오늘 매출: 53,000원, 비교 매출: 20,000원
        return String.format("[%d시] [%s] %.1f%% %s : 오늘 매출: %,d원, 비교 매출: %,d원",
                targetHour, trendPeriod, percentDiffAWeekAgo, trend, todaySales, aWeekAgoSales);
    }

}
