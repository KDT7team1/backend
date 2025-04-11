package com.exam.dashboard;

import java.time.LocalDateTime;
import java.util.Map;

public interface DashboardService {

    Long getTodayVisitors(LocalDateTime now);
    Long getTodaySales(LocalDateTime now);
    Map<String, Object> getTodayAndYesterdaySales(LocalDateTime now);

}
