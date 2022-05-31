package com.angelperez.iobuilderswallets.service;

import com.angelperez.iobuilderswallets.applicationports.WalletsService;
import com.angelperez.iobuilderswallets.common.OperationResult;
import com.angelperez.iobuilderswallets.infrastructureports.DepositsRepositoryPort;
import com.angelperez.iobuilderswallets.infrastructureports.MovementsRepositoryPort;
import com.angelperez.iobuilderswallets.infrastructureports.UsersRepositoryPort;
import com.angelperez.iobuilderswallets.infrastructureports.WalletsRepositoryPort;
import com.angelperez.iobuilderswallets.model.Balance;
import com.angelperez.iobuilderswallets.model.Deposit;
import com.angelperez.iobuilderswallets.model.Movement;
import com.angelperez.iobuilderswallets.model.Wallet;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class WalletsServiceImpl implements WalletsService {

    private WalletsRepositoryPort walletsRepositoryPort;

    private UsersRepositoryPort usersRepositoryPort;

    private MovementsRepositoryPort movementsRepositoryPort;

    private DepositsRepositoryPort depositsRepositoryPort;

    @Override
    public Mono<Wallet> getWallet(String id) {
        return walletsRepositoryPort.getWallet(id);
    }

    @Override
    public Mono<Balance> getWalletBalance(String id) {
        Mono<Wallet> wallet = walletsRepositoryPort.getWallet(id);
        Mono<List<Movement>> movements = movementsRepositoryPort.getMovementsByWalletId(id)
            .collectList();
        Mono<List<Deposit>> deposits = depositsRepositoryPort.getDepositsByWalletId(id)
            .collectList();

        return Mono.zip(wallet, movements, deposits)
            .map(tuple -> new Balance()
                .setId(tuple.getT1().getId())
                .setAlias(tuple.getT1().getAlias())
                .setOwner(tuple.getT1().getOwner())
                .setBalance(tuple.getT1().getBalance())
                .setMovements(tuple.getT2())
                .setDeposits(tuple.getT3()));
    }

    @Override
    public Mono<OperationResult> saveWallet(Wallet wallet) {
        return usersRepositoryPort.existsUser(wallet.getOwner())
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.just(OperationResult.NOT_FOUND);
                } else {
                    return walletsRepositoryPort.saveWallet(wallet.setBalance(BigDecimal.ZERO));
                }
            });
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
