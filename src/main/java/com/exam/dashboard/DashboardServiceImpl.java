package com.exam.dashboard;

import com.exam.saleData.SaleDataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
}
