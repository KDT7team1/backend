package com.exam.disposal;


import com.exam.Inventory.Inventory;
import com.exam.Inventory.InventoryDTO;
import com.exam.Inventory.InventoryRepository;
import com.exam.alert.SseService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisposalServiceImpl implements DisposalService {

    private final SseService sseService;
    InventoryRepository inventoryRepository;
    DisposalRepository disposalRepository;

    public DisposalServiceImpl(InventoryRepository inventoryRepository, DisposalRepository disposalRepository, SseService sseService) {
        this.inventoryRepository = inventoryRepository;
        this.disposalRepository = disposalRepository;
        this.sseService = sseService;
    }

    // 유통기한 지난 재고 조회
    // Disposal 테이블에 폐기 기록 저장
    @Override
    public void checkDisposal() {
        List<Inventory> expiredStocks = inventoryRepository.findAll().stream()
                .filter((item) ->
                        item.getExpirationDate() != null &&
                                item.getExpirationDate().isBefore(LocalDateTime.now()) &&
                                item.getStockQuantity() > 0)
                .toList();


        for(Inventory inventory : expiredStocks) {
            // 폐기 정보 저장하기
            Disposal disposal = Disposal.builder()
                    .goods(inventory.getGoods())
                    .inventory(inventory)
                    .disposed_quantity(inventory.getStockQuantity())
                    .disposal_reason("유통기한 만료")
                    .build();
            disposalRepository.save(disposal);


            // 재고 업데이트
            inventory.setStockQuantity(0L);
            inventory.setStockStatus("폐기");
            inventory.setStockUpdateAt(LocalDateTime.now());
            inventoryRepository.save(inventory);
        }

        if(!expiredStocks.isEmpty()){
            sseService.sendNotification( "admin","자동폐기", "⏰ 자동 폐기된 상품이 " + expiredStocks.size() + "건 있습니다.");
        }

        // sseService.sendNotification("admin", "테스트", "📢 SSE 알림 테스트 전송됨");

        System.out.println("✅ 폐기 처리 완료: " + expiredStocks.size() + "건");

    }

    @Override
    public List<DisposalDTO> findAllDisposal() {
        List<DisposalDTO> dtoList =  disposalRepository.findAll().stream()
                .map((item) ->
                {
                    DisposalDTO dto = DisposalDTO.builder()
                            .disposal_id(item.getDisposal_id())
                            .goods_id(item.getGoods().getGoods_id())
                            .goods_name(item.getGoods().getGoods_name())
                            .batch_id(item.getInventory().getBatchId())
                            .disposal_reason(item.getDisposal_reason())
                            .disposed_quantity(item.getDisposed_quantity())
                            .disposed_at(item.getDisposed_at())
                            .build();
                    return  dto;
                }).collect(Collectors.toList());

        return dtoList;
    }

    // 날짜별로 폐기 테이블 조회
    @Override
    public List<DisposalDTO> findByDisposedAtDate(LocalDate selectedDate) {
        List<Disposal> list = disposalRepository.findByDisposedAtDate(selectedDate);

        List<DisposalDTO> dtoList = list.stream().map(d -> DisposalDTO.builder()
                .disposal_id(d.getDisposal_id())
                .goods_id(d.getGoods().getGoods_id())
                .batch_id(d.getInventory().getBatchId())
                .disposal_reason(d.getDisposal_reason())
                .disposed_quantity(d.getDisposed_quantity())
                .disposed_at(d.getDisposed_at())
                .goods_name(d.getGoods().getGoods_name())
                .build()
        ).toList();

        return dtoList;
    }

    // 폐기 예정인 상품들 조회
//    @Override
//    public List<InventoryDTO> findExpiredButNotDisposed() {
//        List<Inventory> list = inventoryRepository.findExpiredButNotDisposed();
//
//        return list.stream().map(i -> InventoryDTO.builder()
//                .batchId(i.getBatchId())
//                .goodsId(i.getGoods().getGoods_id())
//                .goodsName(i.getGoods().getGoods_name())
//                .stockQuantity(i.getStockQuantity())
//                .stockStatus(i.getStockStatus())
//                .stockUpdateAt(i.getStockUpdateAt())
//                .expirationDate(i.getExpirationDate())
//                .build()
//        ).toList();
//    }


    // 수동 폐기 작업
//    @Override
//    public void manualDispose(List<Long> selectedBatchIds) {
//
//        List<Inventory> list = inventoryRepository.findAllById(selectedBatchIds);
//
//        for(Inventory inventory : list) {
//            if(inventory.getStockQuantity() > 0 ){
//                Disposal disposal = Disposal.builder()
//                        .goods(inventory.getGoods())
//                        .inventory(inventory)
//                        .disposed_quantity(inventory.getStockQuantity())
//                        .disposal_reason("유통기한 만료(수동)")
//                        .build();
//
//                disposalRepository.save(disposal);
//
//                inventory.setStockQuantity(0L);
//                inventory.setStockStatus("폐기");
//                inventory.setStockUpdateAt(LocalDateTime.now());
//                inventoryRepository.save(inventory);
//            }
//        }
//
//        System.out.println("✅ 수동 폐기 처리 완료: " + selectedBatchIds.size() + "건");
//
//    }

    @Override
    public List<DisposalStatsDTO> getDisposalStatsByMonth(int month, int year) {
        return disposalRepository.findMonthlyDisposal(month, year);
    }

    // 폐기 비율
    @Override
    public List<DisposalRateDTO> getDisposalRateStats(List<String> subNames, int month, int year) {
        return disposalRepository.getDisposalRate(subNames, month, year);
    }







}
