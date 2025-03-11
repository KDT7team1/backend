package com.exam.statistics;

import java.time.LocalDate;
import java.util.List;

public interface SalesDailyService {

    // 1) 선택한 날짜의 매출 통계 - 24시간 데이터를 모두 가져옴
    List<SalesDailyDTO> findBySalesDate(LocalDate searchDate);

}
