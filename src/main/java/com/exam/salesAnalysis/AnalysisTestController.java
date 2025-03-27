package com.exam.salesAnalysis;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class AnalysisTestController {

    private final SalesAnomalyScheduler salesAnomalyScheduler;

    public AnalysisTestController(SalesAnomalyScheduler salesAnomalyScheduler) {
        this.salesAnomalyScheduler = salesAnomalyScheduler;
    }

    @GetMapping("/run-sales-analysis")
    public ResponseEntity<String> runSalesAnalysis() {
        salesAnomalyScheduler.runSalesAnalysis();
        return ResponseEntity.ok("매출 분석 실행됨");
    }
}
