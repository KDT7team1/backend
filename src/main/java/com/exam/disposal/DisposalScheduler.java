package com.exam.disposal;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DisposalScheduler {

    private final DisposalService disposalService;

    public DisposalScheduler(DisposalService disposalService) {
        this.disposalService = disposalService;
    }

    @Scheduled(cron = "0 0 * * * *") // 매일 정시마다 실행
    public void runCheckDisposal() {
        disposalService.checkDisposal();
        System.out.println("⏰ 자동 폐기 처리 실행됨 (1시간 주기)");
    }
}
