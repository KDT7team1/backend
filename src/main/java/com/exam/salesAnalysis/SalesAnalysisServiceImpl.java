package com.exam.salesAnalysis;

import com.exam.cartAnalysis.entity.SaleData;
import com.exam.cartAnalysis.repository.SaleDataRepository;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
    public List<SalesProductDTO> getSoldProductsByDateAndHour(LocalDate targetDate, int targetHour) {
        // 특정 날짜와 시간대의 매출 데이터 상세조회 - 시간대와 상품
        LocalDateTime startTime = LocalDateTime.of(targetDate, LocalTime.of(targetHour, 0, 0));
        LocalDateTime endTime = LocalDateTime.of(targetDate, LocalTime.of(targetHour, 59, 59));

        List<SaleData> salesList = dataRepository.findSalesByDateTimeRange(startTime, endTime);

        // 상품별 매출 & 판매량 합산
        Map<String, SalesProductDTO> productSalesMap = salesList.stream()
                .collect(Collectors.toMap(
                        sale -> sale.getGoods().getGoods_name(),
                        sale -> new SalesProductDTO(
                                sale.getGoods().getGoods_name(), // 상품명
                                sale.getSaleAmount(),            // 판매수량
                                sale.getSalePrice(),             // 총판매액
                                0L                               // 매출차이 -> 나중에 업데이트
                        ),
                        (dto1, dto2) -> new SalesProductDTO(
                                dto1.getProductName(),
                                dto1.getTotalAmount() + dto2.getTotalAmount(),
                                dto1.getTotalPrice() + dto2.getTotalPrice(),
                                0L
                        )
                ));

        // DTO 리스트로 변환
        return productSalesMap.values().stream().toList();
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

        // 해당 시간대의 매출액과 상품 목록 조회
        List<SalesProductDTO> todayProducts = getSoldProductsByDateAndHour(targetDate, targetHour);
        long todaySales = todaySalesList.stream().mapToLong(SalesDailyDTO::getDailyPrice).sum();

        // 7일 전 같은 시간대의 매출액과 상품 조회
        LocalDate aWeekAgo = targetDate.minusDays(7);
        List<SalesDailyDTO> aWeekAgoSalesList = getSalesDailyByDateAndHour(aWeekAgo, targetHour);
        long aWeekAgoSales = aWeekAgoSalesList.stream().mapToLong(SalesDailyDTO::getDailyPrice).sum();
        List<SalesProductDTO> aWeekAgoProducts = getSoldProductsByDateAndHour(aWeekAgo, targetHour);

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
        if (Math.abs(percentDiffAWeekAgo) >= 50) {
            // 매출 차이가 큰 상품들을 계산
            List<SalesProductDTO> topProductsAWeekAgo = findTopChangingProducts(aWeekAgo, targetHour);
            String alertMessage = generateAlertMessage(targetHour, percentDiffAWeekAgo, todaySales, aWeekAgoSales, topProductsAWeekAgo);
            SalesAlertDTO alertDTO = createSalesAlertDTO(1, targetDate, targetHour, aWeekAgoSales, todaySales, diffAWeekAgo, alertMessage);
            alertService.save(alertDTO);// 바로 DB에 저장
        }

        // 7일 평균 대비 비교
        if (Math.abs(percentDiffAvg7Days) >= 200) {
            String alertMessage = generateAlertMessage(7, targetHour, percentDiffAvg7Days, todaySales, avg7DaysSales);
            SalesAlertDTO alertDTO = createSalesAlertDTO(7, targetDate, targetHour, avg7DaysSales, todaySales, diffAvg7Days, alertMessage);
            alertService.save(alertDTO);// 바로 DB에 저장
        }

        // 30일 평균 대비 비교
        if (Math.abs(percentDiffAvg30Days) >= 200) {
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

    private String generateAlertMessage(int targetHour, double percentDiffAWeekAgo, long todaySales, long comparisonSales, List<SalesProductDTO> topProducts) {
        String trend = (percentDiffAWeekAgo > 0) ? "상승" : "하락";
        String trendPeriod = "[동요일] 일주일 전 같은 요일 대비";

        StringBuilder productInfo = new StringBuilder();
        for (SalesProductDTO product : topProducts) {
            productInfo.append(String.format("\n - %s: %,d원 변화", product.getProductName(), product.getSalesDiff()));
        }

        return String.format("[%d시] [%s] %.1f%% %s : 오늘 매출: %,d원, 비교 매출: %,d원 \n 가장 큰 판매량 변화를 보인 top3 상품은 다음과 같습니다. \n%s", targetHour, trendPeriod, percentDiffAWeekAgo, trend, todaySales, comparisonSales, productInfo);
    }

    private String generateAlertMessage(int trendBasis, int targetHour, double percentDiffAWeekAgo, long todaySales, long comparisonSales) {
        String trend = (percentDiffAWeekAgo > 0) ? "상승" : "하락";
        String trendPeriod = switch (trendBasis) {
            case 7 -> "[단기 트렌드] 7일 평균 대비";
            case 30 -> "[장기 트렌드] 30일 평균 대비";
            default -> throw new IllegalStateException("Unexpected value: " + trendBasis); // 예외 처리
        };

        return String.format("[%d시] [%s] %.1f%% %s : 오늘 매출: %,d원, 비교 매출: %,d원", targetHour, trendPeriod, percentDiffAWeekAgo, trend, todaySales, comparisonSales);
    }

    // 매출 변화가 큰 상품들을 구하는 메서드
    private List<SalesProductDTO> findTopChangingProducts(LocalDate targetDate, int targetHour) {
        // 오늘과 7일 전 같은 시간대에 팔린 상품들을 비교하는 로직
        List<SalesProductDTO> todayProducts = getSoldProductsByDateAndHour(targetDate, targetHour);
        List<SalesProductDTO> aWeekAgoProducts = getSoldProductsByDateAndHour(targetDate.minusDays(7), targetHour);

        List<SalesProductDTO> resultList = todayProducts.stream()
                .map(product -> {
                    long todaySales = product.getTotalPrice();
                    long aWeekAgoSales = aWeekAgoProducts.stream()
                            .filter(p -> p.getProductName().equals(product.getProductName()))
                            .mapToLong(SalesProductDTO::getTotalPrice)
                            .sum();
                    long diffSales = todaySales - aWeekAgoSales;
                    product.setSalesDiff(diffSales);
                    return product;
                }).collect(Collectors.toList());

        // 판매 변화량의 절대값이 큰 상품 3개만 출력
        return resultList.stream()
                .sorted(Comparator.comparingDouble((SalesProductDTO product) -> Math.abs(product.getSalesDiff())).reversed())
                .limit(3)  // 상위 3개 상품만 가져옴
                .collect(Collectors.toList());
    }
}
