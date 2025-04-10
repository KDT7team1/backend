package com.exam.saleData;



import com.exam.payments.Payments;
import com.exam.payments.PaymentsRepository;
import com.exam.salesHistory.ReceiptDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SaleDataServiceImpl implements SaleDataService {

    private final SaleDataRepository saleDataRepository;
    private final PaymentsRepository paymentsRepository;

    public SaleDataServiceImpl(SaleDataRepository saleDataRepository, PaymentsRepository paymentsRepository) {
        this.saleDataRepository = saleDataRepository;
        this.paymentsRepository = paymentsRepository;
    }

    @Override
    public List<SalesChartDTO> getWeeklySalesByGoodsId(Long goodsId) {
        LocalDateTime today = LocalDate.now().atTime(23, 59, 59);
        LocalDateTime sevenDaysAgo = LocalDate.now().minusDays(6).atStartOfDay(); // 7일

        List<Object[]> rawResults = saleDataRepository.findWeeklySalesByGoodsId(goodsId, sevenDaysAgo, today);

        return rawResults.stream()
                .map(row -> new SalesChartDTO(((java.sql.Date) row[0]).toLocalDate(), ((Number) row[1]).intValue()))
                .collect(Collectors.toList());
    }

    @Override
    public ReceiptDTO getReceiptByOrdersId(Long ordersId) {
        List<SaleData> saleDataList = saleDataRepository.findByOrdersIdWithDetails(ordersId);
        if (saleDataList.isEmpty()) {
            throw new IllegalArgumentException("판매 데이터 없음: " + ordersId);
        }

        Payments payment = paymentsRepository.findByOrdersId(ordersId)
                .orElseThrow(() -> new IllegalArgumentException("결제 정보 없음: " + ordersId));

        return ReceiptDTO.builder()
                .saleDate(saleDataList.get(0).getSaleDate())
                .items(saleDataList.stream()
                        .map(s -> ReceiptDTO.ReceiptItem.builder()
                                .goodsName(s.getGoods().getGoods_name())
                                .saleAmount(s.getSaleAmount())
                                .salePrice(s.getSalePrice())
                                .build())
                        .collect(Collectors.toList()))
                .totalPrice(payment.getPaymentAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paymentApproved(payment.getPaymentApproved())
                .paymentStatus(String.valueOf(saleDataList.get(0).getOrders().getPaymentStatus()))
                .build();
    }
}
