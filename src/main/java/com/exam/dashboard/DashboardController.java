package com.exam.dashboard;

import com.exam.Inventory.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    InventoryRepository inventoryRepository;
    DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService, InventoryRepository inventoryRepository) {
        this.dashboardService = dashboardService;
        this.inventoryRepository = inventoryRepository;
    }

    @GetMapping("/visitors")
    public Long getTodayVisitors() {
        LocalDateTime now = LocalDateTime.now();
        log.info("LOGGER: [Dashboard] 현재 {} 까지의 방문자 수 조회를 요청함", now);
        return dashboardService.getTodayVisitors(now);
    }

    @GetMapping("/sales")
    public Long getTodaySales() {
        LocalDateTime now = LocalDateTime.now();
        log.info("LOGGER: [Dashboard] 현재 {} 까지의 매출액 조회를 요청함", now);
        return dashboardService.getTodaySales(now);
    }

    @GetMapping("/salesAndDiff")
    public ResponseEntity<Map<String, Object>> getTodayAndYesterdaySales() {
        LocalDateTime now = LocalDateTime.now();
        log.info("LOGGER: [Dashboard] 현재 {} 까지의 매출액 조회를 요청함", now);
        Map<String, Object> data = dashboardService.getTodayAndYesterdaySales(now);
        return ResponseEntity.status(200).body(data);
    }

}
