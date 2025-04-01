package com.exam.orderRequest;

import com.exam.goods.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRequestRepository extends JpaRepository<OrderRequest,Long> {
    List<OrderRequest> findByStatusAndScheduledTimeBefore(String status, LocalDateTime time);

    // 가장 최근 발주1건 가져오기
    OrderRequest findTop1ByGoodsOrderByScheduledTimeDesc(Goods goods);
}
