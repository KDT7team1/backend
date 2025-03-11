package com.exam.statistics;

import java.time.LocalDateTime;
import java.util.List;

public interface SalesDailyService {

    // 1) 선택한 날짜의 매출 통계
    List<SalesDailyDTO> findBySalesDate(LocalDateTime startDay, LocalDateTime endDay);

}
