package com.exam.disposal;

public interface DisposalService {

    // 폐기 상품을 등록
    void register(DisposalDTO dto);

    // 폐기 상품의 수량을 재고수량에서 감산
}
