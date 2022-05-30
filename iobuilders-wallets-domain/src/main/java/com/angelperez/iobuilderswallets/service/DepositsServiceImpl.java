package com.angelperez.iobuilderswallets.service;

import com.angelperez.iobuilderswallets.applicationports.DepositsService;
import com.angelperez.iobuilderswallets.common.OperationResult;
import com.angelperez.iobuilderswallets.infrastructureports.DepositsRepositoryPort;
import com.angelperez.iobuilderswallets.infrastructureports.WalletsRepositoryPort;
import com.angelperez.iobuilderswallets.model.Deposit;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
public class DepositsServiceImpl implements DepositsService {

    private WalletsRepositoryPort walletsRepositoryPort;

    private DepositsRepositoryPort depositsRepositoryPort;

    @Override
    public Mono<OperationResult> saveDeposit(Deposit deposit) {
        return walletsRepositoryPort.getWallet(deposit.getWalletId())
            .flatMap(wallet -> depositsRepositoryPort.saveDeposit(deposit)
                .flatMap(result -> {
                    if (result == OperationResult.OK){
                        return walletsRepositoryPort.updateWallet(wallet.setBalance(wallet.getBalance().add(deposit.getAmount())));
                    } else {
                        return Mono.just(result);
                    }
                }))
            .defaultIfEmpty(OperationResult.NOT_FOUND)
            .doOnError(err -> log.error(String.format("Error searching for wallet with id [%s]", deposit.getWalletId()), err))
            .onErrorReturn(OperationResult.ERROR);
    }
}
