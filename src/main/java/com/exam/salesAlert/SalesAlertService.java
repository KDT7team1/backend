package com.exam.salesAlert;

import java.time.LocalDate;
import java.util.List;

public interface SalesAlertService {

    // 특정 날짜의 이상치 알림기록 조회
    List<SalesAlertDTO> findByAlertDate(LocalDate alertDate);

    // 날짜 범위로 이상치 알림기록 조회
    List<SalesAlertDTO> findByAlertDateBetween(LocalDate startDate, LocalDate endDate);

    // 입력받은 날짜의 트렌드 기반 알림기록 조회 7: 일주일 전, 30: 한달 전, 365: 일년 전
    List<SalesAlertDTO> findByTrendBasis(LocalDate alertDate, int trendBasis);

    // 이상치 데이터 저장
    void save(SalesAlertDTO salesAlertDTO);

    // 사용자가 코멘트를 작성하거나 수정
    void updateUserComment(Long alertId, String userComment);

    // 이상치 기록 삭제
    void deleteByAlertId(Long alertId);

}
