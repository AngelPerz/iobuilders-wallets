package com.angelperez.iobuilderswallets.adapter;

import com.angelperez.iobuilderswallets.common.OperationResult;
import com.angelperez.iobuilderswallets.infrastructureports.DepositsRepositoryPort;
import com.angelperez.iobuilderswallets.mapper.DepositEntitiesMapper;
import com.angelperez.iobuilderswallets.model.Deposit;
import com.angelperez.iobuilderswallets.r2dbc.repository.DepositsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@AllArgsConstructor
public class DepositsRepositoryPortAdapter implements DepositsRepositoryPort {

    private DepositsRepository depositsRepository;

    private DepositEntitiesMapper depositEntitiesMapper;

    @Override
    public Mono<OperationResult> saveDeposit(Deposit deposit) {
        return depositsRepository.save(depositEntitiesMapper.toEntity(deposit)
                .setNew(true)
                .setCreationTime(LocalDateTime.now()))
            .doOnError(err -> log.error(String.format("Error saving deposit for wallet [%s]", deposit.getWalletId()), err))
            .map(savedEntity -> OperationResult.OK)
            .onErrorReturn(OperationResult.ERROR);
    }

    @Override
    public Flux<Deposit> getDepositsByWalletId(String walletId) {
        return depositsRepository.findAllByWalletIdOrderByCreationTimeDesc(walletId)
            .map(depositEntitiesMapper::toDomainModel);
    }
}
