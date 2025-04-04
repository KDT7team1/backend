package com.exam.cartAnalysis.dto;
import com.exam.member.Member;
import com.exam.payments.PaymentStatus;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class OrdersDTO {

    Long ordersId;
    Long memberNo;
    LocalDateTime ordersDate;
    Long finalPrice;
    String orderSummary;
    PaymentStatus paymentStatus;

    // ✅ 프론트에서 넘어오는 상품 리스트를 담는 필드
    private List<OrderItemDTO> orderItems;

    // ✅ 주문 상세를 담는 내부 클래스
    @Data
    public static class OrderItemDTO {
        private Long goodsId;
        private Integer saleAmount;
        private Long salePrice;
    }

}
