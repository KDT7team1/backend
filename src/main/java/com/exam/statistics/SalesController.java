package com.exam.statistics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.DateTimeException;
import java.time.LocalDate;
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

        // 날짜 포매팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            // 날짜 데이터 타입 변환
            LocalDate searchDate = LocalDate.parse(salesDate, formatter);

            log.info("LOGGER: 조회할 날짜: {}", salesDate);

            List<SalesDailyDTO> salesDaily = salesDailyService.findBySalesDate(salesDate);
            log.info("LOGGER: salesDaily 정보 획득: {}", salesDaily);

            return ResponseEntity.status(200).body(salesDaily);
        } catch (DateTimeException e) {
            log.error("날짜 형식이 올바르지 않습니다.", e);
            return ResponseEntity.badRequest().body(null);
        }
    } // end findBySalesDate

    @GetMapping("statistics/salesMontly/{salesMonth}")
    public ResponseEntity<List<SalesMonthlyDTO>> findBySalesMonth(@PathVariable String salesMonth) {
        log.info("LOGGER: 월간 매출 조회를 요청함");
        log.info("LOGGER: 조회할 월: {}", salesMonth);

        List<SalesMonthlyDTO> salesMonthly = salesMonthlyService.findBySalesMonth(salesMonth);
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
        LocalDateTime todayStart = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime todayEnd = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 두 날짜의 매출 정보를 저장할 MAP
        Map<String, List<SalesDailyDTO>> salesDiff = new HashMap<>();

        try {
            // 오늘 날짜의 매출 정보 획득
            List<SalesDailyDTO> todaySales = salesDailyService.findBySalesDate(todayStart, todayEnd);
            log.info("LOGGER: 오늘 날짜의 매출 정보 획득: {}", todaySales);

            // 비교할 날짜의 매출 정보 획득
            LocalDate target = LocalDate.parse(targetDate, formatter);
            LocalDateTime targetStart = target.atStartOfDay(); // 00:00:00
            LocalDateTime targetEnd = target.atTime(23, 59, 59); // 23:59:59

            List<SalesDailyDTO> targetDateSales = salesDailyService.findBySalesDate(targetStart, targetEnd);
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
    public ResponseEntity<Map<String, List<SalesMonthlyDTO>>> getMontlySalesDiff(@PathVariable String targetMonth) {
        log.info("LOGGER: 이번 달과 월간 매출 비교를 요청함");
        log.info("LOGGER: 이번 달: 2024-12 비교할 월: {}", targetMonth);

        String thisMonth = "2024-12";

        Map<String, List<SalesMonthlyDTO>> salesDiff = new HashMap<>();

        List<SalesMonthlyDTO> thisMonthSales = salesMonthlyService.findBySalesMonth(thisMonth);
        log.info("LOGGER: thisMonthSales 정보 획득: {}", thisMonthSales);
        salesDiff.put("thisMonth", thisMonthSales);

        List<SalesMonthlyDTO> targetMonthSales = salesMonthlyService.findBySalesMonth(targetMonth);
        log.info("LOGGER: targetMonth 정보 획득: {}", targetMonthSales);
        salesDiff.put("targetMonth", targetMonthSales);

        return ResponseEntity.status(200).body(salesDiff);
    } // end findBySalesMonth
}
