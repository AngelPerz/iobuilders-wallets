package com.angelperez.iobuilderswallets.service;

import com.angelperez.iobuilderswallets.applicationports.WalletsService;
import com.angelperez.iobuilderswallets.common.OperationResult;
import com.angelperez.iobuilderswallets.infrastructureports.UsersRepositoryPort;
import com.angelperez.iobuilderswallets.infrastructureports.WalletsRepositoryPort;
import com.angelperez.iobuilderswallets.model.Wallet;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
@AllArgsConstructor
public class WalletsServiceImpl implements WalletsService {

    private WalletsRepositoryPort walletsRepositoryPort;

    private UsersRepositoryPort usersRepositoryPort;

    @Override
    public Mono<Wallet> getWallet(String id) {
        return walletsRepositoryPort.getWallet(id);
    }

    @Override
    public Mono<OperationResult> saveWallet(Wallet wallet) {
        usersRepositoryPort.existsUser(wallet.getOwner()).subscribe(t -> log.info(t.toString()));

        wallet.setBalance(BigDecimal.ZERO);
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
