package com.exam.salesAlert;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesAlertRepository extends JpaRepository<SalesAlert, Long> {

    // 특정 날짜의 이상치 알림기록 조회
    @Query("SELECT s FROM SalesAlert s WHERE s.alertDate = :alertDate ORDER BY s.alertHour")
    List<SalesAlert> findByAlertDate(LocalDate alertDate);

    // 날짜 범위로 이상치 알림기록 조회
    @Query("SELECT s FROM SalesAlert s WHERE s.alertDate between :startDate and :endDate ORDER BY s.alertHour")
    List<SalesAlert> findByAlertDateBetween(LocalDate startDate, LocalDate endDate);

    // 입력받은 날짜의 트렌드 기반 알림기록 조회 1: 전주 동요일, 7: 단기 트렌드, 30: 장기 트렌드
    @Query("SELECT s FROM SalesAlert s WHERE s.trendBasis = :trendBasis AND s.alertDate = :alertDate ORDER BY s.alertHour")
    List<SalesAlert> findByTrendBasis(@Param("alertDate") LocalDate alertDate, @Param("trendBasis") int trendBasis);
    
    // 사용자가 코멘트를 작성하거나 수정
    @Modifying
    @Query("UPDATE SalesAlert s SET s.userComment = :userComment WHERE s.alertId = :alertId")
    void updateUserComment(Long alertId, String userComment);

    // 이상치 기록 삭제
    @Modifying
    @Query("DELETE FROM SalesAlert s WHERE s.alertId = :alertId")
    void deleteByAlertId(@Param("alertId") Long alertId);

}
