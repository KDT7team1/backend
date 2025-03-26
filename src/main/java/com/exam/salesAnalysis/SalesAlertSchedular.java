package com.exam.salesAnalysis;

import com.exam.statistics.SalesDailyDTO;
import com.exam.statistics.SalesDailyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class SalesAlertSchedular {

    SalesDailyService salesDailyService;
    SalesAlertService salesAlertService;

    public SalesAlertSchedular(SalesDailyService salesDailyService, SalesAlertService salesAlertService) {
        this.salesDailyService = salesDailyService;
        this.salesAlertService = salesAlertService;
    }

    // 설정 가능한 변동률 임계값
    @Value("${sales.anomaly.threshold:30}")
    private double ANOMALY_THRESHOLD;

    // 매 시 정각마다 실행
    @Scheduled(cron = "0 0 * * * *")
    public void detectAnomalies() {
        log.info("[Schedular] 매출 이상 감지 시작: {}", LocalDateTime.now());

        // 현재 시간 기준으로 한 시간 전 데이터를 가져옴
        LocalDateTime referenceTime = LocalDateTime.now().minusHours(1);
        LocalDate referenceDate = referenceTime.toLocalDate();
        int referenceHour = referenceTime.getHour();

        try {
            processHourlySalesAnalysis(referenceDate, referenceHour);
        } catch (Exception e) {
            log.error("LOGGER: [Schedular] 매출 이상 감지 중 오류 발생", e);
        }
    }

    private void processHourlySalesAnalysis(LocalDate referenceDate, int referenceHour) {
        List<SalesDailyDTO> salesData = salesDailyService.getHourlySalesByDate(referenceDate);

        Optional<List<SalesDailyDTO>> filteredSales = Optional.of(salesData.stream()
                .filter(s -> s.getSalesHour() == referenceHour)
                .collect(Collectors.toList()));

        filteredSales.filter(list -> !list.isEmpty())
                .ifPresentOrElse(
                        recentSales -> analyzeAndAlertSales(recentSales, referenceDate, referenceHour),
                        () -> log.info("{}시의 매출 데이터가 없습니다.", referenceHour)
                );
    }

    private void analyzeAndAlertSales(List<SalesDailyDTO> recentSales, LocalDate referenceDate, int referenceHour) {
        recentSales.forEach(sales -> {
            SalesComparisonResult comparisonResult = performSalesComparison(sales, referenceDate, referenceHour);
            checkAndSaveAlerts(sales, comparisonResult);
        });
    }

    private SalesComparisonResult performSalesComparison(SalesDailyDTO sales, LocalDate referenceDate, int referenceHour) {
        return new SalesComparisonResult(
                getSalesByComparison(sales, referenceDate, referenceHour, 1),
                getSalesByComparison(sales, referenceDate, referenceHour, 7),
                getSalesByComparison(sales, referenceDate, referenceHour, 30)
        );
    }

    private void checkAndSaveAlert(SalesDailyDTO sales, SalesComparisonResult result) {
        checkAndCreateAlert(sales, result.getWeekAgoSales(), 1);
        checkAndCreateAlert(sales, result.getWeekAvgSales(), 7);
        checkAndCreateAlert(sales, result.getMonthAvgSales(), 30);
    }

    private void checkAndCreateAlert(SalesDailyDTO sales, long comparisonSales, int basis) {
        if (comparisonSales == 0) return;

        long difference = sales.getDailyPrice() - comparisonSales;
        double percentage = ((double) difference / comparisonSales) * 100;

        if (Math.abs(percentage) >= ANOMALY_THRESHOLD) {
            createAndSaveAlert(sales, comparisonSales, basis, difference, percentage);
        }
    }

    private void createAndSaveAlert(SalesDailyDTO sales, long comparisonSales, int basis,
                                    long difference, double percentage) {
        String basisText = resolveBasisText(basis);
        String message = formatAlertMessage(basisText, sales.getSalesHour(),
                (int)percentage, Math.abs(difference));

        SalesAlertDTO alert = SalesAlertDTO.builder()
                .trendBasis(basis)
                .alertDate(sales.getSalesDate())
                .previousSales(comparisonSales)
                .currentSales(sales.getDailyPrice())
                .difference(difference)
                .alertMessage(message)
                .build();

        salesAlertService.save(alert);
        log.info("[Alert] {}", message);
    }

    private String resolveBasisText(int basis) {
        return switch (basis) {
            case 1 -> "7일 전 비교";
            case 7 -> "7일 평균 비교";
            case 30 -> "30일 평균 비교";
            default -> throw new IllegalArgumentException("트렌드 기준값이 유효하지 않습니다.");
        };
    }

    private String formatAlertMessage(String basisText, int hour, int percentage, long difference) {
        return String.format("%s %d시 매출이 %d%%(%d원) %s했습니다.",
                basisText, hour, percentage, difference,
                (percentage > 0) ? "증가" : "감소");
    }

    private Long getSalesByComparison(SalesDailyDTO sales, LocalDate referenceDate,
                                      int referenceHour, int trendBasis) {
        LocalDate startDate = referenceDate;
        LocalDate endDate = referenceDate;

        return switch (trendBasis) {
            case 1 -> {
                startDate = referenceDate.minusDays(7);
                endDate = referenceDate.minusDays(7);
                yield getComparisonSales(startDate, endDate, referenceHour);
            }
            case 7 -> {
                startDate = referenceDate.minusDays(7);
                endDate = referenceDate.minusDays(1);
                yield getComparisonSales(startDate, endDate, referenceHour);
            }
            case 30 -> {
                startDate = referenceDate.minusDays(30);
                endDate = referenceDate.minusDays(1);
                yield getComparisonSales(startDate, endDate, referenceHour);
            }
            default -> throw new IllegalArgumentException("트렌드 기준값이 유효하지 않습니다.");
        };
    }

    private Long getComparisonSales(LocalDate startDate, LocalDate endDate, int referenceHour) {
        List<SalesDailyDTO> salesData = salesDailyService.getHourlySalesByDate(startDate);

        return salesData.stream()
                .filter(s -> s.getSalesHour() == referenceHour)
                .mapToLong(SalesDailyDTO::getDailyPrice)
                .sum();
    }

    // 내부 비교 결과 클래스
    private static class SalesComparisonResult {
        private final long weekAgoSales;
        private final long weekAvgSales;
        private final long monthAvgSales;

        public SalesComparisonResult(long weekAgoSales, long weekAvgSales, long monthAvgSales) {
            this.weekAgoSales = weekAgoSales;
            this.weekAvgSales = weekAvgSales;
            this.monthAvgSales = monthAvgSales;
        }

        // Getters
        public long getWeekAgoSales() { return weekAgoSales; }
        public long getWeekAvgSales() { return weekAvgSales; }
        public long getMonthAvgSales() { return monthAvgSales; }
    }
}
