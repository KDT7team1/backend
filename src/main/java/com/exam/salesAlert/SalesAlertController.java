package com.exam.salesAlert;

import com.exam.statistics.SalesDailyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/salesAlert")
public class SalesAlertController {

    SalesAlertService alertService;
    SalesDailyService dailyService;

    public SalesAlertController(SalesAlertService alertService, SalesDailyService dailyService) {
        this.alertService = alertService;
        this.dailyService = dailyService;
    }

    @GetMapping("/searchList/byDate/{date}")
    public ResponseEntity<List<SalesAlertDTO>> findByAlertDate(@PathVariable String date) {
        // 특정 날짜의 이상치 알림기록 조회
        log.info("LOGGER: 일간 이상치 알림기록 조회를 요청함");

        // 날짜 포매팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            // 날짜 데이터 타입 변환
            LocalDate searchDate = LocalDate.parse(date, formatter);

            log.info("LOGGER: 조회할 날짜: {}", searchDate);

            List<SalesAlertDTO> alertList = alertService.findByAlertDate(searchDate);
            log.info("LOGGER: 해당하는 날짜의 알림 정보 획득: {}", alertList);

            return ResponseEntity.status(200).body(alertList);
        } catch (DateTimeException e) {
            log.error("날짜 형식이 올바르지 않습니다.", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/searchList/byDate/{date1}/{date2}")
    public ResponseEntity<List<SalesAlertDTO>> findByAlertDate(@PathVariable String date1, @PathVariable String date2) {
        // 특정 기간의 이상치 알림기록 조회
        log.info("LOGGER: 특정 기간의 이상치 알림기록 조회를 요청함");

        // 날짜 포매팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            // 날짜 데이터 타입 변환
            LocalDate searchDate1 = LocalDate.parse(date1, formatter);
            LocalDate searchDate2 = LocalDate.parse(date2, formatter);

            log.info("LOGGER: 조회할 기간: {} ~ {}", date1, date2);

            List<SalesAlertDTO> alertList = alertService.findByAlertDateBetween(searchDate1, searchDate2);
            log.info("LOGGER: 해당하는 기간의 알림 정보 획득: {}", alertList);

            return ResponseEntity.status(200).body(alertList);
        } catch (DateTimeException e) {
            log.error("날짜 형식이 올바르지 않습니다.", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/searchList/byTrend/{date}/{trendBasis}")
    public ResponseEntity<List<SalesAlertDTO>> findByTrendBasis(@PathVariable String date, @PathVariable int trendBasis) {
        /*
        date: 분석 데이터를 조회할 날짜
        trendBasis: 트렌드의 타입 | 1: [요일 트렌드] 일주일 전 같은 요일, 7 : [단기 트렌드] 일주일 평균, 30: [장기 트렌드] 한달 평균
         */
        // 특정 날짜의 이상치 알림기록 조회
        log.info("LOGGER: 트렌드 타입에 해당하는 일간 이상치 알림기록 조회를 요청함");

        // 날짜 포매팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            // 날짜 데이터 타입 변환
            LocalDate searchDate = LocalDate.parse(date, formatter);

            log.info("LOGGER: 조회할 날짜: {}, 트렌드 타입: {}", searchDate, trendBasis);

            List<SalesAlertDTO> alertList = alertService.findByTrendBasis(searchDate, trendBasis);
            log.info("LOGGER: 해당하는 날짜와 트렌드 타입의 알림 정보 획득: {}", alertList);

            return ResponseEntity.status(200).body(alertList);
        } catch (DateTimeException e) {
            log.error("날짜 형식이 올바르지 않습니다.", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/updateComment")
    public ResponseEntity<String> updateUserComment(@RequestParam Long alertId, @RequestParam String userComment) {
        try {
            alertService.updateUserComment(alertId, userComment);
            return ResponseEntity.status(200).body("코멘트가 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("수정에 실패했습니다.");
        }
    }

    @DeleteMapping("/deleteAlert")
    public ResponseEntity<String> deleteByAlertId(@RequestParam Long alertId) {
        try {
            alertService.deleteByAlertId(alertId);
            return ResponseEntity.status(200).body("정상적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("삭제에 실패했습니다.");
        }
    }

}
