package com.exam.salesAnalysis;

import com.exam.salesAlert.SalesAlertDTO;
import com.exam.salesAlert.SalesAlertService;
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

        // 데이터가 이미 처리되었는지 확인
        boolean isDataProcessed = isDataProcessed(targetDate);

        if (!isDataProcessed) {
            SalesAlertDTO alertDTO = salesAnalysisService.detectSalesAnomaly(targetDate, targetHour);
            if (alertDTO != null) {
                salesAlertService.save(alertDTO);
                log.info("LOGGER: [매출기록 분석기] 이상 매출 감지: {}", alertDTO.getAlertMessage());
            } else {
                log.info("LOGGER: [매출기록 분석기] 정상 매출");
            }
        } else {
            log.info("LOGGDER: [매출기록 분석기] 이미 처리된 데이터입니다. 스케줄러를 실행하지 않습니다.");
        }
    }

    // 해당 시간대에 이상치 알림이 이미 존재하는지 확인하는 메서드
    private boolean isDataProcessed(LocalDate targetDate) {
        // 해당 날짜에 대한 이상치 알림 기록을 조회
        List<SalesAlertDTO> existingAlerts = salesAlertService.findByAlertDate(targetDate);

        // 이미 알림이 존재하는지 확인 (매출이 동일한 경우)
        return existingAlerts.stream()
                .anyMatch(alert -> alert.getAlertDate().equals(targetDate));
    }
}
