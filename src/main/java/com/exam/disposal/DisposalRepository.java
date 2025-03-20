package com.exam.disposal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisposalRepository extends JpaRepository<Disposal, Long> {

    // 폐기 목록에 해당하는 상품을 등록

    // 폐기 상품의 수량을 재고수량에서 감산
}
