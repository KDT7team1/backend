package com.exam.salesAnalysis;

import com.exam.salesAlert.SalesAlertDTO;
import com.exam.statistics.SalesDaily;
import com.exam.statistics.SalesDailyDTO;
import com.exam.statistics.SalesDailyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesAnalysisServiceImpl implements SalesAnalysisService{

    SalesDailyRepository dailyRepository;
    SalesDataRepository dataRepository;

    public SalesAnalysisServiceImpl(SalesDailyRepository dailyRepository, SalesDataRepository dataRepository) {
        this.dailyRepository = dailyRepository;
        this.dataRepository = dataRepository;
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
    public SalesAlertDTO detectSalesAnomaly(LocalDate salesDate, int salesHour) {
        return null;
    }


}
