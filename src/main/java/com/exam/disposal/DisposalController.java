package com.exam.disposal;

import com.exam.Inventory.InventoryDTO;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/disposal") // 해당 컨트롤러의 모든
public class DisposalController {

    DisposalService disposalService;

    public DisposalController(DisposalService disposalService) {
        this.disposalService = disposalService;
    }

    // 폐기 처리하기
    @PostMapping("/check-expired")
    public ResponseEntity<String> checkExpired() {
        disposalService.checkDisposal();
        return ResponseEntity.ok("✅ 유통기한 지난 재고가 폐기 처리되었습니다.");
    }

    // 폐기 테이블 가져오기 ( 전체 )
    @GetMapping("/findAll")
    public ResponseEntity<List<DisposalDTO>> findAllDisposal() {
        List<DisposalDTO> list = disposalService.findAllDisposal();
        return ResponseEntity.status(200).body(list);
    }

    // 날짜별로 폐기테이블 조회
    @GetMapping("/by-date")
    public ResponseEntity<List<DisposalDTO>> getDisposalsByDate(
            @RequestParam("date") String date) {

        LocalDate selectedDate = LocalDate.parse(date); // "2025-03-24"

        // 날짜에 맞는 테이블만 조회하기
        List<DisposalDTO> list = disposalService.findByDisposedAtDate(selectedDate);
        return ResponseEntity.status(200).body(list);


    }

    // 유통기한 만료지만 폐기처리되지 않은 상품 조회
    @GetMapping("/pending-disposal")
    public ResponseEntity<List<InventoryDTO>> getPendingDisposalStocks() {
        List<InventoryDTO> list = disposalService.findExpiredButNotDisposed();
        return ResponseEntity.ok(list);
    }

    // 수동 폐기 처리 ( 선택한 제품만 )
    @PostMapping("/manual-dispose")
    public ResponseEntity<String> manualDispose(
            @RequestBody List<Long> selectedBatchIds
    ) {
        disposalService.manualDispose(selectedBatchIds);
        return ResponseEntity.ok("✅ 선택한 항목이 수동 폐기 처리되었습니다.");
    }

    // 폐기 통계 (월별, 카테고리별)
    @GetMapping("/stats")
    public ResponseEntity<List<DisposalStatsDTO>> getMonthlyDisposalStats(
            @RequestParam("month") int month,
            @RequestParam("year") int year
    ) {
        List<DisposalStatsDTO> list = disposalService.getDisposalStatsByMonth(month, year);
        return ResponseEntity.ok(list);
    }


}