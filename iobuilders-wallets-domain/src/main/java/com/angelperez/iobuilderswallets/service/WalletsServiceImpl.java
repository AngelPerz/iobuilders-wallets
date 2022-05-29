package com.angelperez.iobuilderswallets.service;

import com.angelperez.iobuilderswallets.applicationports.WalletsService;
import com.angelperez.iobuilderswallets.common.OperationResult;
import com.angelperez.iobuilderswallets.infrastructureports.WalletsRepositoryPort;
import com.angelperez.iobuilderswallets.model.Wallet;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class WalletsServiceImpl implements WalletsService {

    private WalletsRepositoryPort walletsRepositoryPort;

    @Override
    public Mono<Wallet> getWallet(String id) {
        return walletsRepositoryPort.getWallet(id);
    }

    @Override
    public Mono<OperationResult> saveWallet(Wallet wallet) {
        return walletsRepositoryPort.saveWallet(wallet);
    }

    @Override
    public Mono<OperationResult> updateWallet(Wallet wallet) {
        return walletsRepositoryPort.updateWallet(wallet);
    }

    @Override
    public Mono<OperationResult> deleteWallet(String id) {
        return walletsRepositoryPort.deleteWallet(id);
    }

}
