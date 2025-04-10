package com.exam.salesHistory;

import com.exam.cartAnalysis.dto.OrdersDTO;
import com.exam.cartAnalysis.service.OrdersService;
import com.exam.payments.PaymentStatus;
import com.exam.saleData.SaleDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/salesHistory")
public class SalesHistoryController {

    OrdersService ordersService;
    SaleDataService saleDataService;

    public SalesHistoryController(OrdersService ordersService, SaleDataService saleDataService) {
        this.ordersService = ordersService;
        this.saleDataService = saleDataService;
    }

    @GetMapping("/findByDate")
    public ResponseEntity<Page<OrdersDTO>> getOrdersListByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "paymentStatus", required = false) Integer paymentStatusCode,
            @PageableDefault(size = 20, sort = "ordersDate", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("LOGGER: 판매 기록 조회를 요청함: 판매일자: {}", date);
        PaymentStatus paymentStatus = null;
        if (paymentStatusCode != null) {
            paymentStatus = PaymentStatus.fromCode(paymentStatusCode);
        }
        Page<OrdersDTO> result = ordersService.getOrdersListByDate(date, paymentStatus, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/receipt/{ordersId}")
    public ResponseEntity<ReceiptDTO> getReceipt(@PathVariable Long ordersId) {
        log.info("LOGGER: 판매 기록 상세조회를 요청함: 주문번호: {}", ordersId);
        ReceiptDTO receipt = saleDataService.getReceiptByOrdersId(ordersId);
        return ResponseEntity.ok(receipt);
    }

}