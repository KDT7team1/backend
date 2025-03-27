package com.exam.salesAlert;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
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
                    .alertDate(s.getAlertDate())
                    .alertHour(s.getAlertHour())
                    .previousSales(s.getPreviousSales())
                    .currentSales(s.getCurrentSales())
                    .difference(s.getDifference())
                    .alertMessage(s.getAlertMessage())
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
                    .alertDate(s.getAlertDate())
                    .alertHour(s.getAlertHour())
                    .previousSales(s.getPreviousSales())
                    .currentSales(s.getCurrentSales())
                    .difference(s.getDifference())
                    .alertMessage(s.getAlertMessage())
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
                    .alertDate(s.getAlertDate())
                    .alertHour(s.getAlertHour())
                    .previousSales(s.getPreviousSales())
                    .currentSales(s.getCurrentSales())
                    .difference(s.getDifference())
                    .alertMessage(s.getAlertMessage())
                    .userComment(s.getUserComment())
                    .build();
            return dto;
        }).collect(Collectors.toList());

        return dtoList;
    }

    @Override
    public void save(SalesAlertDTO salesAlertDTO) {
        SalesAlert salesAlert = SalesAlert.builder()
                .alertId(salesAlertDTO.getAlertId())
                .trendBasis(salesAlertDTO.getTrendBasis())
                .alertDate(salesAlertDTO.getAlertDate())
                .alertHour(salesAlertDTO.getAlertHour())
                .previousSales(salesAlertDTO.getPreviousSales())
                .currentSales(salesAlertDTO.getCurrentSales())
                .difference(salesAlertDTO.getDifference())
                .alertMessage(salesAlertDTO.getAlertMessage())
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
