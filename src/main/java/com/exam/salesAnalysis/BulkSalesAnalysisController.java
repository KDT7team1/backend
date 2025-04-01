package com.exam.salesAnalysis;


import com.exam.salesAlert.SalesAlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/sales-analysis")
public class BulkSalesAnalysisController {

    private final SalesAnalysisService salesAnalysisService;
    private final SalesAlertService salesAlertService;

    public BulkSalesAnalysisController(SalesAnalysisService salesAnalysisService, SalesAlertService salesAlertService) {
        this.salesAnalysisService = salesAnalysisService;
        this.salesAlertService = salesAlertService;
    }

    @GetMapping("/generate-bulk-data")
    public ResponseEntity<String> generateBulkData() {
        log.info("[매출 분석] 2024년 10월 ~ 2025년 4월 데이터 생성 시작");

        LocalDate startDate = LocalDate.of(2024, 10, 1);
        LocalDate endDate = LocalDate.of(2025, 4, 30);

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            for (int hour = 0; hour < 24; hour++) {
                // 이미 처리된 데이터인지 확인
                if (!isDataProcessed(currentDate, hour)) {
                    salesAnalysisService.detectSalesAnomaly(currentDate, hour);
                    log.info("[매출 분석] {}일 {}시 데이터 생성 완료", currentDate, hour);
                } else {
                    log.info("[매출 분석] {}일 {}시는 이미 처리됨, 건너뜀", currentDate, hour);
                }
            }
            currentDate = currentDate.plusDays(1);
        }

        log.info("[매출 분석] 데이터 생성 완료");
        return ResponseEntity.ok("매출 분석 데이터가 성공적으로 생성되었습니다.");
    }

    private boolean isDataProcessed(LocalDate targetDate, int targetHour) {
        return salesAlertService.findByAlertDate(targetDate).stream()
                .anyMatch(alert -> alert.getAlertHour() == targetHour);
    }
}
