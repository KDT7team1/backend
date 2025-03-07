package com.exam.statistics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class SalesController {

    SalesDailyService salesDailyService;
    SalesMonthlyService salesMonthlyService;

    public SalesController(SalesDailyService salesDailyService, SalesMonthlyService salesMonthlyService) {
        this.salesDailyService = salesDailyService;
        this.salesMonthlyService = salesMonthlyService;
    }

    @GetMapping("/statistics/salesDaily/{salesDate}")
    public ResponseEntity<List<SalesDailyDTO>> findBySalesDate(@PathVariable String salesDate) {
        log.info("LOGGER: 일간 매출 조회를 요청함");
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

    @GetMapping("statistics/salesMontly/{salesMonth}")
    public ResponseEntity<SalesMonthlyDTO> findBySalesMonth(@PathVariable String salesMonth) {
        log.info("LOGGER: 월간 매출 조회를 요청함");
        log.info("LOGGER: 조회할 월: {}", salesMonth);

        SalesMonthlyDTO salesMonthly = salesMonthlyService.findBySalesMonth(salesMonth);
        log.info("LOGGER: salesMonth 정보 획득: {}", salesMonthly);

        return ResponseEntity.status(200).body(salesMonthly);
    } // end findBySalesMonth

    @GetMapping("statistics/salesYearly/{year}")
    public ResponseEntity<List<SalesMonthlyDTO>> findBySalesYear(@PathVariable String year) {
        log.info("LOGGER: 연간 매출 조회를 요청함");
        log.info("LOGGER: 조회할 연도: {}", year);

        String searchyear = year + "%";
        List<SalesMonthlyDTO> salesMonthlyDTOList = salesMonthlyService.findBySalesYear(searchyear);

        log.info("LOGGER: 조회한 연도의 데이터: {}", salesMonthlyDTOList);
        return ResponseEntity.status(200).body(salesMonthlyDTOList);
    } // end findBySalesYear

    @GetMapping("statistics/salesDailyDiff/{targetDate}")
    public ResponseEntity<Map<String, List<SalesDailyDTO>>> getDailySalesDiff(@PathVariable String targetDate) {
        log.info("LOGGER: 오늘과 특정 날짜의 매출 비교를 요청함");
        log.info("LOGGER: 오늘 날짜: 2024-12-31, 비교할 날짜: {}", targetDate);
        // 날짜 포맷 변환
        targetDate = targetDate + " 00:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime today = LocalDateTime.parse("2024-12-31 00:00:00", formatter);

        // 두 날짜의 매출 정보를 MAP에 저장
        Map<String, List<SalesDailyDTO>> salesDiff = new HashMap<>();

        try {
            List<SalesDailyDTO> todaySales = salesDailyService.findBySalesDate(today);
            log.info("LOGGER: 오늘 날짜의 매출 정보 획득: {}", todaySales);

            LocalDateTime searchDate = LocalDateTime.parse(targetDate, formatter);
            List<SalesDailyDTO> targetDateSales = salesDailyService.findBySalesDate(searchDate);
            log.info("LOGGER: 비교할 날짜의 매출 정보 획득: {}", targetDateSales);

            salesDiff.put("today", todaySales);
            salesDiff.put("target", targetDateSales);

            return ResponseEntity.status(200).body(salesDiff);
        } catch (DateTimeException e) {
            log.error("날짜 형식이 올바르지 않습니다.", e);
            return ResponseEntity.badRequest().body(null);
        }
    } // end getSalesDifference

    @GetMapping("statistics/salesMonthlyDiff/{targetMonth}")
    public ResponseEntity<Map<String, SalesMonthlyDTO>> getMontlySalesDiff(@PathVariable String targetMonth) {
        log.info("LOGGER: 이번 달과 월간 매출 비교를 요청함");
        log.info("LOGGER: 이번 달: 2024-12 비교할 월: {}", targetMonth);

        String thisMonth = "2024-12";

        Map<String, SalesMonthlyDTO> salesDiff = new HashMap<>();

        SalesMonthlyDTO thisMonthSales = salesMonthlyService.findBySalesMonth(thisMonth);
        log.info("LOGGER: thisMonthSales 정보 획득: {}", thisMonthSales);
        salesDiff.put("thisMonth", thisMonthSales);

        SalesMonthlyDTO targetMonthSales = salesMonthlyService.findBySalesMonth(targetMonth);
        log.info("LOGGER: targetMonth 정보 획득: {}", targetMonthSales);
        salesDiff.put("targetMonth", targetMonthSales);

        return ResponseEntity.status(200).body(salesDiff);
    } // end findBySalesMonth
}
