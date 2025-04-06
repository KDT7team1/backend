package com.exam.salesAnalysis;


import com.exam.saleData.SaleData;
import com.exam.saleData.SaleDataRepository;
import com.exam.salesAlert.SalesAlertDTO;
import com.exam.salesAlert.SalesAlertRepository;
import com.exam.salesAlert.SalesAlertService;
import com.exam.statistics.SalesDaily;
import com.exam.statistics.SalesDailyDTO;
import com.exam.statistics.SalesDailyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public List<SalesProductDTO> getSoldProductsByDateAndHour(LocalDate targetDate, int targetHour) {
        // 특정 날짜와 시간대의 매출 데이터 상세조회 - 시간대와 상품
        LocalDateTime startTime = LocalDateTime.of(targetDate, LocalTime.of(targetHour, 0, 0));
        LocalDateTime endTime = LocalDateTime.of(targetDate, LocalTime.of(targetHour, 59, 59));

        List<SaleData> salesList = dataRepository.findSalesByDateTimeRange(startTime, endTime);

        // 상품별 매출 & 판매량 합산
        Map<Long, SalesProductDTO> productSalesMap = new HashMap<>();

        for (SaleData sale : salesList) {
            Long productId = sale.getGoods().getGoods_id();
            String productName = sale.getGoods().getGoods_name();
            long saleAmount = sale.getSaleAmount();
            long salePrice = sale.getSalePrice() * sale.getSaleAmount();

            // 기존에 존재하면 합산, 없으면 추가
            productSalesMap.merge(productId,
                    new SalesProductDTO(productId, productName, saleAmount, salePrice, 0L),
                    (existing, newItem) -> {
                        existing.setTotalAmount(existing.getTotalAmount() + newItem.getTotalAmount());
                        existing.setTotalPrice(existing.getTotalPrice() + newItem.getTotalPrice());
                        return existing;
                    }
            );
        }

        // DTO 리스트로 변환 + 매출액 기준 내림차순 정렬
        return productSalesMap.values().stream()
                .sorted(Comparator.comparing(SalesProductDTO::getTotalPrice).reversed())
                .collect(Collectors.toList());
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

    record ComparisonBasis(
            int trendBasis, // 1, 2, 3 (일주일, 한 달, 일 년 전)
            LocalDate compareDate,
            long previousSales,
            long diffPrice,
            double percentDiff
    ) {}

    @Override
    public void detectSalesAnomaly(LocalDate targetDate, int targetHour) {
        List<SalesDailyDTO> todaySalesList = getSalesDailyByDateAndHour(targetDate, targetHour);
        if (todaySalesList.isEmpty()) {
            return; // 오늘의 데이터가 없으면 처리하지 않음
        }

        // 해당 시간대의 매출액 조회
        long todaySales = todaySalesList.stream().mapToLong(SalesDailyDTO::getDailyPrice).sum();

        // 비교 기준 리스트 생성
        List<ComparisonBasis> comparisons = Stream.of(
                new AbstractMap.SimpleEntry<>(1, targetDate.minusDays(7)),
                new AbstractMap.SimpleEntry<>(2, targetDate.minusMonths(1)),
                new AbstractMap.SimpleEntry<>(3, targetDate.minusYears(1))
        ).map(entry -> {
            List<SalesDailyDTO> pastList = getSalesDailyByDateAndHour(entry.getValue(), targetHour);
            long pastSales = pastList.stream().mapToLong(SalesDailyDTO::getDailyPrice).sum();
            long diff = todaySales - pastSales;
            double percent = (pastSales != 0) ? (diff * 100.0 / pastSales) : 0;
            return new ComparisonBasis(entry.getKey(), entry.getValue(), pastSales, diff, percent);
        }).toList();

        // 알림 저장
        for (ComparisonBasis cb: comparisons) {
            // 퍼센트 필터 제외
//            if (Math.abs(cb.percentDiff()) >= 50) {
               String alertMessage = generateAlertMessage(targetDate, cb.trendBasis(), targetHour, cb.percentDiff(), todaySales, cb.previousSales());
               SalesAlertDTO alertDTO = createSalesAlertDTO(
                       cb.trendBasis(), targetDate, targetHour, cb.previousSales(),todaySales,
                       cb.diffPrice(), cb.percentDiff(), alertMessage
               );
               alertService.save(alertDTO);
//            }
        }

    }

    private SalesAlertDTO createSalesAlertDTO(int trendBasis, LocalDate targetDate, int targetHour, long previousSales, long currentSales, long diffPrice, double percent, String alertMessage) {
        return SalesAlertDTO.builder()
                .trendBasis(trendBasis)
                .alertDate(targetDate)
                .alertHour(targetHour)
                .previousSales(previousSales)
                .currentSales(currentSales)
                .difference(diffPrice)
                .percentageDifference(percent)
                .alertMessage(alertMessage)
                .build();
    }

    private String generateAlertMessage(LocalDate targetDate, int trendBasis, int targetHour, double percentDiff, long todaySales, long comparisonSales) {
        String trend = (percentDiff > 0) ? "상승" : "하락";
        String trendPeriod = switch (trendBasis) {
            case 1 -> "일주일 전 대비";
            case 2 -> "1개월 전 대비";
            case 3 -> "1년 전 대비";
            default -> throw new IllegalStateException("Unexpected value: " + trendBasis); // 예외 처리
        };
        String dateStr = targetDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd (E)", Locale.KOREAN));

        return String.format("[%s %d시] [%s] %.1f%% %s  금일 매출: %,d원, 비교 매출: %,d원", dateStr, targetHour, trendPeriod, percentDiff, trend, todaySales, comparisonSales);
    }

}
