package com.exam.salesAnalysis;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SalesAlertServiceImpl implements SalesAlertService{

    SalesAlertRepository salesAlertRepository;

    public SalesAlertServiceImpl(SalesAlertRepository salesAlertRepository) {
        this.salesAlertRepository = salesAlertRepository;
    }

    @Override
    public List<SalesAlertDTO> findByAlertDate(LocalDate alertDate) {
        List<SalesAlert> entityList = salesAlertRepository.findByAlertDate(alertDate);

        List<SalesAlertDTO> dtoList = entityList.stream().map(s -> {
            SalesAlertDTO dto = SalesAlertDTO.builder()
                    .alertId(s.getAlertId())
                    .trendBasis(s.getTrendBasis())
                    .date(s.getDate())
                    .previous(s.getPrevious())
                    .current(s.getCurrent())
                    .difference(s.getDifference())
                    .message(s.getMessage())
                    .userComment(s.getUserComment())
                    .build();
            return dto;
        }).collect(Collectors.toList());

        return dtoList;
    }

    @Override
    public List<SalesAlertDTO> findByAlertDateBetween(LocalDate startDate, LocalDate endDate) {
        List<SalesAlert> entityList = salesAlertRepository.findByAlertDateBetween(startDate, endDate);

        List<SalesAlertDTO> dtoList = entityList.stream().map(s -> {
            SalesAlertDTO dto = SalesAlertDTO.builder()
                    .alertId(s.getAlertId())
                    .trendBasis(s.getTrendBasis())
                    .date(s.getDate())
                    .previous(s.getPrevious())
                    .current(s.getCurrent())
                    .difference(s.getDifference())
                    .message(s.getMessage())
                    .userComment(s.getUserComment())
                    .build();
            return dto;
        }).collect(Collectors.toList());

        return dtoList;
    }

    @Override
    public List<SalesAlertDTO> findByTrendBasis(LocalDate alertDate, int trendBasis) {
        List<SalesAlert> entityList = salesAlertRepository.findByTrendBasis(alertDate, trendBasis);

        List<SalesAlertDTO> dtoList = entityList.stream().map(s -> {
            SalesAlertDTO dto = SalesAlertDTO.builder()
                    .alertId(s.getAlertId())
                    .trendBasis(s.getTrendBasis())
                    .date(s.getDate())
                    .previous(s.getPrevious())
                    .current(s.getCurrent())
                    .difference(s.getDifference())
                    .message(s.getMessage())
                    .userComment(s.getUserComment())
                    .build();
            return dto;
        }).collect(Collectors.toList());

        return dtoList;
    }

    @Override
    public void saveSalesAlert(SalesAlertDTO salesAlertDTO) {
        SalesAlert salesAlert = SalesAlert.builder()
                .trendBasis(salesAlertDTO.getTrendBasis())
                .date(salesAlertDTO.getDate())
                .previous(salesAlertDTO.getPrevious())
                .current(salesAlertDTO.getCurrent())
                .difference(salesAlertDTO.getDifference())
                .message(salesAlertDTO.getMessage())
                .userComment(salesAlertDTO.getUserComment())
                .build();

        salesAlertRepository.save(salesAlert);
    }

    @Override
    public void updateUserComment(Long alertId, String userComment) {
        // 사용자가 수정한 코멘트로 업데이트
        Optional<SalesAlert> optionalSalesAlert = salesAlertRepository.findById(alertId);

        if (optionalSalesAlert.isPresent()) {
            SalesAlert salesAlert = optionalSalesAlert.get();
            salesAlert.setUserComment(userComment);

            salesAlertRepository.save(salesAlert);
        } else {
            throw new RuntimeException("알림을 찾을 수 없습니다. id: " + alertId);
        }
    }

    @Override
    public void deleteByAlertId(Long alertId) {
        // alertId로 해당 알림을 찾고 삭제
        salesAlertRepository.deleteByAlertId(alertId);
    }
}
