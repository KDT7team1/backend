package com.exam.cartAnalysis.repository;


import com.exam.cartAnalysis.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long>{


    
}

