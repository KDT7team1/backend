package com.exam.disposal;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DisposalServiceImpl implements DisposalService{

    DisposalRepository disposalRepository;

    public DisposalServiceImpl(DisposalRepository disposalRepository) {
        this.disposalRepository = disposalRepository;
    }

    @Override
    public void register(DisposalDTO dto) {

        Disposal disposal = Disposal.builder()
                .goodsId(dto.getGoodsId())
                .disposedQuantity(dto.getDisposedQuantity())
                .disposedAt(LocalDateTime.now())
                .build();

        disposalRepository.save(disposal);

    }
}
