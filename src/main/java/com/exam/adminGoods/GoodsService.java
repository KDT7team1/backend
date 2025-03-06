package com.exam.adminGoods;

public interface GoodsService {

        // 상품 저장
        void save(GoodsDTO dto);

        // 상품 수정
        //void update(Long goodsId, GoodsDTO dto);
        // void update(GoodsDTO dto);

        // 상품 삭제
        void delete(Long goodsId);

    }


