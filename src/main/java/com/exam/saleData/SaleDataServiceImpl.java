package com.exam.saleData;


import com.exam.payments.Payments;
import com.exam.payments.PaymentsRepository;
import com.exam.salesHistory.ReceiptDTO;
import com.exam.statistics.SalesDailyRepository;
import com.exam.statistics.SalesMonthlyRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SaleDataServiceImpl implements SaleDataService {

    private final SaleDataRepository saleDataRepository;
    private final PaymentsRepository paymentsRepository;
    private final SalesDailyRepository salesDailyRepository;
    private final SalesMonthlyRepository salesMonthlyRepository;

    public SaleDataServiceImpl(SaleDataRepository saleDataRepository, PaymentsRepository paymentsRepository, SalesDailyRepository salesDailyRepository, SalesMonthlyRepository salesMonthlyRepository) {
        this.saleDataRepository = saleDataRepository;
        this.paymentsRepository = paymentsRepository;
        this.salesDailyRepository = salesDailyRepository;
        this.salesMonthlyRepository = salesMonthlyRepository;
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

    @Transactional
    @Override
    public void updateSalesDaily() {
        LocalDateTime now = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        LocalDateTime oneHourAgo = now.minusHours(1);
        List<Object[]> result = saleDataRepository.findHourlyAggregatedSales(oneHourAgo, now);

        for (Object[] row : result) {
            LocalDate salesDate = ((Date) row[0]).toLocalDate(); // DATE 타입일 경우
            int salesHour = ((Integer) row[1]);
            Long categoryId = ((Number) row[2]).longValue();
            Long subCategoryId = ((Number) row[3]).longValue();
            Long totalPrice = ((Number) row[4]).longValue();
            Long totalAmount = ((Number) row[5]).longValue();

            // upsert 수행
            int updated = salesDailyRepository.updateDailyStat(
                    salesDate, salesHour, categoryId, subCategoryId, totalPrice, totalAmount
            );
            if (updated == 0) {
                salesDailyRepository.insertDailyStat(
                        salesDate, salesHour, categoryId, subCategoryId, totalPrice, totalAmount
                );
            }
        }

    }

    @Transactional
    @Override
    public void updateSalesMonthly() {
        LocalDateTime now = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        YearMonth currentMonth = YearMonth.from(now.minusDays(1)); // 어제 기준 월

        LocalDateTime start = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime end = currentMonth.plusMonths(1).atDay(1).atStartOfDay();

        List<Object[]> result = saleDataRepository.findMonthlyAggregatedSales(start, end);

        for (Object[] row : result) {
            String salesMonth = (String) row[0];
            Long categoryId = ((Number) row[1]).longValue();
            Long subCategoryId = ((Number) row[2]).longValue();
            Long totalPrice = ((Number) row[3]).longValue();
            Long totalAmount = ((Number) row[4]).longValue();

            int updated = salesMonthlyRepository.updateMonthlyStat(
                    salesMonth, categoryId, subCategoryId, totalPrice, totalAmount
            );

            if (updated == 0) {
                salesMonthlyRepository.insertMonthlyStat(
                        salesMonth, categoryId, subCategoryId, totalPrice, totalAmount
                );
            }
        }
    }

}
