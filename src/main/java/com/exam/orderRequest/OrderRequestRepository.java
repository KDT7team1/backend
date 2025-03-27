package com.exam.orderRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRequestRepository extends JpaRepository<OrderRequest,Long> {
    List<OrderRequest> findByStatusAndScheduledTimeBefore(String status, LocalDateTime time);
}
