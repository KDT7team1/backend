package com.exam.statistics;

import java.util.List;

public interface SalesMonthlyService {
    List<SalesMonthlyDTO> findBySalesMonth(String salesMonth);
}
