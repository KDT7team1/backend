package com.exam.dashboard;

import com.exam.Inventory.InventoryRepository;
import com.exam.goods.Goods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final InventoryRepository inventoryRepository;
    DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService, InventoryRepository inventoryRepository) {
        this.dashboardService = dashboardService;
        this.inventoryRepository = inventoryRepository;
    }

    @RequestMapping("/visitors")
    public Long getTodayVisitors() {
        LocalDateTime now = LocalDateTime.now();
        log.info("LOGGER: [Dashboard] 현재 {} 까지의 방문자 수 조회를 요청함", now);
        return dashboardService.getTodayVisitors(now);
    }

    @RequestMapping("/sales")
    public Long getTodaySales() {
        LocalDateTime now = LocalDateTime.now();
        log.info("LOGGER: [Dashboard] 현재 {} 까지의 매출액 조회를 요청함", now);
        return dashboardService.getTodaySales(now);
    }



}
