package com.exam.saleData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SalesSchedular {

    private final SaleDataService saleDataService;

    // 매 시 정각 실행
    @Scheduled(cron = "0 0 * * * *")
    public void runHourlySalesStatUpdate() {
      log.info("📊 [스케줄러] 매시 정각 통계 갱신 시작");
        try {
            saleDataService.updateSalesDaily();
            log.info("✅ [스케줄러] 통계 갱신 완료");
        } catch (Exception e) {
            log.error("❌ [스케줄러] 통계 갱신 중 오류 발생", e);
        }
    }

    // 매일 자정에 실행 (00:00)
    @Scheduled(cron = "0 0 0 1 * *")
    public void runMonthlySalesStatUpdate() {
        log.info("📊 [스케줄러] 월간 통계 갱신 시작");
        try {
            saleDataService.updateSalesMonthly();
            log.info("✅ [스케줄러] 월간 통계 갱신 완료");
        } catch (Exception e) {
            log.error("❌ [스케줄러] 월간 통계 갱신 실패", e);
        }
    }
}
