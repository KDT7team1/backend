package com.exam.Inventory.scheduler;

import com.exam.Inventory.Inventory;
import com.exam.Inventory.InventoryRepository;
import com.exam.notification.SseService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class InventoryScheduler {
    private final InventoryRepository inventoryRepository;
    private final SseService sseService;

    public InventoryScheduler(InventoryRepository inventoryRepository, SseService sseService) {
        this.inventoryRepository = inventoryRepository;
        this.sseService = sseService;
    }

    @Scheduled(cron = "0 0 17 * * *")
    public void notifyTomorrowExpirations(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusHours(24); // 24시간 뒤

        List<Inventory> items = inventoryRepository.findExpiringSoonItems(now, tomorrow);

        if(!items.isEmpty()){
            sseService.sendNotification(
                    "admin", "유통기한임박",
                    "내일(" + tomorrow.toLocalDate() + ") 폐기 예정 상품이 " + items.size() + "개 있습니다."
            );
        }
    }

}
