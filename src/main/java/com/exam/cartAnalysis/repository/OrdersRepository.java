package com.exam.cartAnalysis.repository;

import com.exam.cartAnalysis.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long>{

    @Query("select o from Orders o where o.ordersDate between :start and :end")
    Page<Orders> getOrdersListByDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, Pageable pageable);

}

