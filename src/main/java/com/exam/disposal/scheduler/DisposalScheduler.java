package com.exam.disposal.scheduler;


import com.exam.disposal.DisposalService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DisposalScheduler {

    private final DisposalService disposalService;

    public DisposalScheduler(DisposalService disposalService) {
        this.disposalService = disposalService;
    }

    @Scheduled(cron = "0 0 2 * * *") // 매일 오전 2시
    public void runCheckDisposal() {
        disposalService.checkDisposal();
        System.out.println("⏰ 자동 폐기 처리 실행됨");
    }
}
