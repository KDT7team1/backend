package com.exam.cartAnalysis.repository;

import com.exam.cartAnalysis.entity.Orders;
import com.exam.payments.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long>{

    @Query("select o from Orders o where o.ordersDate between :start and :end order by o.ordersId desc")
    Page<Orders> getOrdersListByDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, Pageable pageable);

    @Query("select o from Orders o where o.ordersDate between :startDate and :endDate and o.paymentStatus = :paymentStatus order by o.ordersId desc")
    Page<Orders> getOrdersListByDateAndPaymentStatus(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("paymentStatus") PaymentStatus paymentStatus,
            Pageable pageable
    );
}

