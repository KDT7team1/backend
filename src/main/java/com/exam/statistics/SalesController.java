package com.exam.statistics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Slf4j
public class SalesController {

    SalesDailyService salesDailyService;
    SalesMonthlyService salesMonthlyService;

    public SalesController(SalesDailyService salesDailyService, SalesMonthlyService salesMonthlyService) {
        this.salesDailyService = salesDailyService;
        this.salesMonthlyService = salesMonthlyService;
    }

    @GetMapping("/admin/salesDaily/{salesDate}")
    public ResponseEntity<List<SalesDailyDTO>> findBySalesDate(@PathVariable String salesDate) {
        log.info("LOGGER: salesDate: {}", salesDate);
        salesDate = salesDate + " 00:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            LocalDateTime searchDate = LocalDateTime.parse(salesDate, formatter);
            log.info("LOGGER: searchDate 포맷 변환: {}", searchDate);

            List<SalesDailyDTO> salesDaily = salesDailyService.findBySalesDate(searchDate);
            log.info("LOGGER: salesDaily 정보 획득: {}", salesDaily);
            return ResponseEntity.status(200).body(salesDaily);
        } catch (DateTimeException e) {
            log.error("날짜 형식이 올바르지 않습니다.", e);
            return ResponseEntity.badRequest().body(null);
        }
    } // end findBySalesDate

    @GetMapping("admin/salesMontly/{salesMonth}")
    public ResponseEntity<List<SalesMonthlyDTO>> findBySalesMonth(@PathVariable String salesMonth) {
        log.info("LOGGER: salesMonth: {}", salesMonth);

        List<SalesMonthlyDTO> salesMonthly = salesMonthlyService.findBySalesMonth(salesMonth);
        log.info("LOGGER: salesDaily 정보 획득: {}", salesMonthly);

        return ResponseEntity.status(200).body(salesMonthly);
    }

}
