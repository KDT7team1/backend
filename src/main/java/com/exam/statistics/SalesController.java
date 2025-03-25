package com.exam.statistics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistics")
@Slf4j
public class SalesController {

    SalesDailyService salesDailyService;
    SalesMonthlyService salesMonthlyService;
    SalesYearlyService salesYearlyService;

    public SalesController(SalesDailyService salesDailyService, SalesMonthlyService salesMonthlyService, SalesYearlyService salesYearlyService) {
        this.salesDailyService = salesDailyService;
        this.salesMonthlyService = salesMonthlyService;
        this.salesYearlyService = salesYearlyService;
    }

    // 시간 - 일 - 월별 매출조회
    @GetMapping("/salesHourlyTotal/{salesDate}")
    public ResponseEntity<List<SalesDailyDTO>> getHourlySalesByDate(@PathVariable String salesDate) {
        log.info("LOGGER: 일간(시간별) 매출 조회를 요청함");

        // 날짜 포매팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            // 날짜 데이터 타입 변환
            LocalDate searchDate = LocalDate.parse(salesDate, formatter);

            log.info("LOGGER: 조회할 날짜: {}", searchDate);

            List<SalesDailyDTO> salesDaily = salesDailyService.getHourlySalesByDate(searchDate);
            log.info("LOGGER: 일간 정보 획득: {}", salesDaily);

            return ResponseEntity.status(200).body(salesDaily);
        } catch (DateTimeException e) {
            log.error("날짜 형식이 올바르지 않습니다.", e);
            return ResponseEntity.badRequest().body(null);
        }
    } // end getHourlySalesByDate

    @GetMapping("/salesDailyTotal/{salesMonth}")
    public ResponseEntity<List<SalesDailyDTO>> getDailySalesByMonth(@PathVariable String salesMonth) {
        log.info("LOGGER: 월간(일별) 매출 조회를 요청함");
        log.info("LOGGER: 조회할 월: {}", salesMonth);

        List<SalesDailyDTO> salesMonthly = salesDailyService.getDailySalesByMonth(salesMonth);
        log.info("LOGGER: 월간 정보 획득: {}", salesMonthly);

        return ResponseEntity.status(200).body(salesMonthly);
    }

    @GetMapping("/salesMonthlyTotal/{salesYear}")
    public ResponseEntity<List<SalesMonthlyDTO>> getMonthlySalesByYear(@PathVariable String salesYear) {
        log.info("LOGGER: 연간(월별) 매출 조회를 요청함");
        log.info("LOGGER: 조회할 연도: {}", salesYear);

        List<SalesMonthlyDTO> salesYearly = salesMonthlyService.getMonthlySalesByYear(salesYear);
        log.info("LOGGER: 연간 정보 획득: {}", salesYearly);

        return ResponseEntity.status(200).body(salesYearly);
    }

    // 카테고리별 매출 조회
    // 일간 - 카테고리 대분류 매출 조회
    @GetMapping("/salesDailyCategory/{salesDate}")
    public ResponseEntity<List<SalesDailyDTO>> getCategorySalesByDate(@PathVariable String salesDate) {
        log.info("LOGGER: 카테고리 대분류별 매출 조회를 요청함");

        // 날짜 포매팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            // 날짜 데이터 타입 변환
            LocalDate searchDate = LocalDate.parse(salesDate, formatter);

            log.info("LOGGER: 조회할 날짜: {}", searchDate);

            List<SalesDailyDTO> salesDaily = salesDailyService.getCategorySalesByDate(searchDate);
            log.info("LOGGER: salesDaily 카테고리 대분류 정보 획득: {}", salesDaily);

            return ResponseEntity.status(200).body(salesDaily);
        } catch (DateTimeException e) {
            log.error("날짜 형식이 올바르지 않습니다.", e);
            return ResponseEntity.badRequest().body(null);
        }
    } // end findBySalesDate

    // 일간 - 카테고리 소분류 매출 조회
    @GetMapping("/salesDailyCategory/{salesDate}/{subCategory}")
    public ResponseEntity<List<SalesDailyDTO>> getSubCategorySalesByDate(@PathVariable String salesDate, @PathVariable Long subCategory) {
        log.info("LOGGER: 카테고리 소분류별 매출 조회를 요청함");

        // 날짜 포매팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            // 날짜 데이터 타입 변환
            LocalDate searchDate = LocalDate.parse(salesDate, formatter);
            log.info("LOGGER: 조회할 날짜: {}, 조회할 대분류: {}", searchDate, subCategory);

            List<SalesDailyDTO> salesDaily = salesDailyService.getSubCategorySalesByDate(searchDate, subCategory);
            log.info("LOGGER: salesDaily 카테고리 소분류 정보 획득: {}", salesDaily);

            return ResponseEntity.status(200).body(salesDaily);
        } catch (DateTimeException e) {
            log.error("날짜 형식이 올바르지 않습니다.", e);
            return ResponseEntity.badRequest().body(null);
        }
    } // end getSubCategorySalesByDate

    // 월간 - 카테고리별 대분류 매출 조회
    @GetMapping("/salesMonthlyCategory/{salesMonth}")
    public ResponseEntity<List<SalesMonthlyDTO>> getCategorySalesByMonth(@PathVariable String salesMonth) {
        log.info("LOGGER: 월간 카테고리 대분류 매출 조회를 요청함");
        log.info("LOGGER: 조회할 월: {}", salesMonth);

        List<SalesMonthlyDTO> salesMonthly = salesMonthlyService.getCategorySalesByMonth(salesMonth);
        log.info("LOGGER: salesMonth 카테고리 대분류 정보 획득: {}", salesMonthly);

        return ResponseEntity.status(200).body(salesMonthly);
    } // end findBySalesMonth

    // 월간 - 카테고리 소분류 매출 조회
    @GetMapping("/salesMonthlyCategory/{salesMonth}/{categoryId}")
    public ResponseEntity<List<SalesMonthlyDTO>> getSubCategorySalesByMonth(@PathVariable String salesMonth, @PathVariable Long categoryId) {
        log.info("LOGGER: 월간 카테고리 소분류 매출 조회를 요청함");
        log.info("LOGGER: 조회할 월: {}, 조회할 카테고리: {}", salesMonth, categoryId);

        List<SalesMonthlyDTO> salesMonthly = salesMonthlyService.getSubCategorySalesByMonth(salesMonth, categoryId);
        log.info("LOGGER: salesMonth 카테고리 소분류 정보 획득: {}", salesMonthly);

        return ResponseEntity.status(200).body(salesMonthly);
    } // end getSubCategorySalesByMonth

    // 연간 - 카테고리 대분류 매출 조회
    @GetMapping("/salesYearlyCategory/{salesYear}")
    public ResponseEntity<List<SalesYearlyDTO>> getCategorySalesByYear(@PathVariable String salesYear) {
        log.info("LOGGER: 연간 카테고리 대분류 매출 조회를 요청함");
        log.info("LOGGER: 조회할 연도: {}", salesYear);

        List<SalesYearlyDTO> salesYearly = salesYearlyService.getCategorySalesByYear(salesYear);
        log.info("LOGGER: salesYearly 카테고리 대분류 정보 획득: {}", salesYearly);

        return ResponseEntity.status(200).body(salesYearly);
    }

    // 연간 - 카테고리 소분류 매출 조회
    @GetMapping("/salesYearlyCategory/{salesYear}/{categoryId}")
    public ResponseEntity<List<SalesYearlyDTO>> getSubCategorySalesByYear(@PathVariable String salesYear, @PathVariable Long categoryId) {
        log.info("LOGGER: 연간 카테고리 소분류 매출 조회를 요청함");
        log.info("LOGGER: 조회할 연도: {}, 조회할 카테고리: {}", salesYear, categoryId);

        List<SalesYearlyDTO> salesYearly = salesYearlyService.getSubCategorySalesByYear(salesYear, categoryId);
        log.info("LOGGER: salesMonth 카테고리 소분류 정보 획득: {}", salesYearly);

        return ResponseEntity.status(200).body(salesYearly);
    }

    // 매출 평균 조회 -> salesAnalysis/SalesAlertController
//    public ResponseEntity<SalesDailyDTO>
}
