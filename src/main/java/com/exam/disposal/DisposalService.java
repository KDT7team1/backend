package com.exam.disposal;


import com.exam.Inventory.InventoryDTO;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface DisposalService {

    // 폐기 수동 처리
    void checkDisposal();

    // 폐기 테이블 전체 조회
    List<DisposalDTO> findAllDisposal();

    // 날짜별로 폐기 테이블 조회
    List<DisposalDTO> findByDisposedAtDate(LocalDate selectedDate);

    // 폐기처리 안 된 상품
    List<InventoryDTO> findExpiredButNotDisposed();

    // 수동 폐기 처리
    @Transactional
    void manualDispose(List<Long> selectedBatchIds);

    // 폐기통계 (월별/카테고리별)
    List<DisposalStatsDTO> getDisposalStatsByMonth(int month, int year);

}
