package com.exam.disposal;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/disposal")
@Slf4j
public class DisposalController {

    DisposalService disposalService;

    public DisposalController(DisposalService disposalService) {
        this.disposalService = disposalService;
    }

    @PutMapping("/register")
    public ResponseEntity<DisposalDTO> register(@RequestBody @Valid DisposalDTO dto) {
        log.info("LOGGER: 상품 폐기처리를 요청함 : 폐기할 상품 : id: {} - {} 개", dto.goodsId, dto.disposedQuantity);
        try {
            disposalService.register(dto);
            log.info("LOGGER: 상품 폐기처리를 완료함 : 처리시간: {} - id: {} - {}개", LocalDateTime.now(), dto.goodsId, dto.disposedQuantity);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto); // 201
        } catch (Exception e) {
            log.error("LOGGER: 상품 폐기처리 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }
}

