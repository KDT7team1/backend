package com.exam.salesHistory;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceiptDTO {
    private LocalDateTime saleDate;
    private List<ReceiptItem> items;
    private Long totalPrice;
    private String paymentMethod;
    private LocalDateTime paymentApproved;
    private String paymentStatus;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReceiptItem {
        private String goodsName;
        private Long saleAmount;
        private Long salePrice;
    }
}
