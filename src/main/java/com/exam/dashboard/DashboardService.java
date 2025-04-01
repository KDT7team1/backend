package com.exam.dashboard;

import java.time.LocalDateTime;

public interface DashboardService {

    Long getTodayVisitors(LocalDateTime now);

    Long getTodaySales(LocalDateTime now);

}
