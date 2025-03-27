package com.exam.salesAnalysis;

import com.exam.salesAlert.SalesAlertDTO;
import com.exam.salesAlert.SalesAlertService;
import com.exam.statistics.SalesDailyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class SalesAnomalyScheduler {

    SalesAnalysisService salesAnalysisService;
    SalesAlertService salesAlertService;


    public SalesAnomalyScheduler(SalesAnalysisService salesAnalysisService, SalesAlertService salesAlertService) {
        this.salesAnalysisService = salesAnalysisService;
        this.salesAlertService = salesAlertService;
    }

    @Scheduled(cron = "0 0 * * * *") // 매 정각(00분 00초)에 실행
    public void runSalesAnalysis() {

        LocalDateTime now = LocalDateTime.now();
        log.info("LOGGER: [매출기록 분석기] 자동 실행 - 시스템 시간: {}", now);

        LocalDateTime target = now.minusHours(1);
        LocalDate targetDate = target.toLocalDate();
        int targetHour = target.getHour();

        // 오늘 매출 데이터 가져오기
        List<SalesDailyDTO> todaySalesList = salesAnalysisService.getSalesDailyByDateAndHour(targetDate, targetHour);
        long todaySales = todaySalesList.stream().mapToLong(SalesDailyDTO::getDailyPrice).sum();

        // 데이터가 이미 처리되었는지 확인
        boolean isDataProcessed = isDataProcessed(targetDate, targetHour);

        if (!isDataProcessed) {
            log.info("LOGGER: [매출기록 분석기] 매출 기록 분석을 시작합니다.");
            salesAnalysisService.detectSalesAnomaly(targetDate, targetHour);
        } else {
            log.info("LOGGDER: [매출기록 분석기] 이미 처리된 데이터입니다. 스케줄러를 실행하지 않습니다.");
        }
    }

    // 해당 시간대에 이상치 알림이 이미 존재하는지 확인하는 메서드
    private boolean isDataProcessed(LocalDate targetDate, int targetHour) {
        // 해당 날짜에 대한 이상치 알림 기록을 조회
        List<SalesAlertDTO> existingAlerts = salesAlertService.findByAlertDate(targetDate);

        // 해당 날짜와 시간에 이상치 알림이 있는 경우 스케줄러를 실행하지 않음
        return existingAlerts.stream()
                .anyMatch(alert -> alert.getAlertDate() == targetDate && alert.getAlertHour() == targetHour);
    }
}
