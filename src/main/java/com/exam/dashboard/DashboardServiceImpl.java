package com.exam.dashboard;

import com.exam.saleData.SaleDataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    SaleDataRepository saleDataRepository;

    public DashboardServiceImpl(SaleDataRepository saleDataRepository) {
        this.saleDataRepository = saleDataRepository;
    }

    @Override
    public Long getTodayVisitors(LocalDateTime now) {
        // 오늘의 방문자 수 = 판매 횟수 총합
        LocalDateTime startTime = now.toLocalDate().atStartOfDay();
        return saleDataRepository.getTodayVisitors(startTime, now);
    }

    @Override
    public Long getTodaySales(LocalDateTime now) {
        // 오늘의 매출액 = 판매액 총합
        LocalDateTime startTime = now.toLocalDate().atStartOfDay();
        return saleDataRepository.getTodaySales(startTime, now);
    }

    @Override
    public Map<String, Object> getTodayAndYesterdaySales(LocalDateTime now) {
        Map<String, Object> result = new HashMap<>();

        LocalDateTime todayStart = now.toLocalDate().atStartOfDay();
        LocalDateTime yesterdaySameTime = now.minusDays(1);
        LocalDateTime yesterdayStart = yesterdaySameTime.toLocalDate().atStartOfDay();

        Long todaySales = saleDataRepository.getTodaySales(todayStart, now);
        Long yesterdaySales = saleDataRepository.getTodaySales(yesterdayStart, yesterdaySameTime);

        double difference;

        // Sales 값이 0이 아니면 차이를 계산
        if (yesterdaySales != 0) {
            difference = (double)(todaySales - yesterdaySales) / yesterdaySales * 100.0;
        } else {
            difference = 0.0d; // 어제 매출이 0이면 변화율은 0으로 처리
        }

        // 차이 값을 백분율로 반올림 (소수점 한 자리까지)
        double rounded = Math.round(difference * 10.0) / 10.0;

        result.put("today", todaySales);
        result.put("yesterday", yesterdaySales);
        result.put("difference", rounded);  // 백분율 차이

        return result;
    }
}
