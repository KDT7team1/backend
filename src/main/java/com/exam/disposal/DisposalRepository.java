package com.exam.disposal;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DisposalRepository extends JpaRepository<Disposal, Long> {

    // 날짜별로 폐기 테이블 확인하기
    @Query("SELECT d FROM Disposal d where Date(d.disposed_at) = :selectedDate")
    List<Disposal> findByDisposedAtDate(@Param("selectedDate") LocalDate selectedDate);

}

