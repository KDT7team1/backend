package com.exam.goods.scheduler;

import com.exam.goods.Goods;
import com.exam.goods.GoodsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class restoreOriginalPriceScheduler {

    GoodsRepository goodsRepository;

    @Autowired // ✅ 생성자 주입 시 생략 가능하지만 명확하게!
    public restoreOriginalPriceScheduler(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    @Scheduled(cron = "0 * * * * *")  // 이전 실행 종료 후 10초 뒤 실행
    public void restoreOriginalPriceIfExpired() {
        List<Goods> expired = goodsRepository
                .findAllByDiscountEndAtBefore(LocalDateTime.now());

        for (Goods g : expired) {
            if (g.getOriginalPrice() != null) {
                g.setGoods_price(g.getOriginalPrice());
                g.setOriginalPrice(null);
                g.setDiscountRate(null);
                g.setDiscountEndAt(null);
                g.setGoods_updated_at(LocalDateTime.now());

                goodsRepository.save(g);
            }
        }
    }

}
