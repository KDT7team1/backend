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

    // 월별로 폐기 조회하기 (카테고리별)
    @Query("""
    select new com.exam.disposal.DisposalStatsDTO(s.sub_name,SUM(d.disposed_quantity))
    from Disposal d
    join d.goods g
    join g.subCategory s
    where function('MONTH', d.disposed_at) = :month
      and function('YEAR', d.disposed_at) = :year 
    group by s.sub_name
    """)
    List<DisposalStatsDTO> findMonthlyDisposal(@Param("month") int month, @Param("year") int year);

}

