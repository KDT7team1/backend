package com.exam.dashboard;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @RequestMapping("/visitors/{now}")
    public Long getTodayVisitors(@PathVariable LocalDateTime now) {
        log.info("LOGGER: [Dashboard] 현재 {} 까지의 방문자 수 조회를 요청함", now);
        return dashboardService.getTodayVisitors(now);
    }

    @RequestMapping("/sales/{now}")
    public Long getTodaySales(@PathVariable LocalDateTime now) {
        log.info("LOGGER: [Dashboard] 현재 {} 까지의 매출액 조회를 요청함", now);
        return dashboardService.getTodaySales(now);
    }
}
