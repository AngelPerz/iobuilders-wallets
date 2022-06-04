package com.angelperez.iobuilderswallets.implementation;

import com.angelperez.iobuilderswallets.applicationports.MovementsService;
import com.angelperez.iobuilderswallets.common.OperationResult;
import com.angelperez.iobuilderswallets.infrastructureports.MovementsRepositoryPort;
import com.angelperez.iobuilderswallets.infrastructureports.WalletsRepositoryPort;
import com.angelperez.iobuilderswallets.model.Movement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
public class MovementsServiceImpl implements MovementsService {

    private WalletsRepositoryPort walletsRepositoryPort;

    private MovementsRepositoryPort movementsRepositoryPort;

    @Override
    public Mono<OperationResult> saveMovement(Movement movement) {
        return Flux.just(movement.getOriginWallet(), movement.getDestinyWallet())
            .flatMap(walletId -> walletsRepositoryPort.getWallet(walletId))
            .doOnError(err -> log.error(String.format("Error searching for wallets with ids [%s, %s] saving a movement",
                movement.getOriginWallet(), movement.getDestinyWallet()), err))
            .collectList()
            .flatMap(wallets -> {
                if (wallets.size() != 2) {
                    return Mono.just(OperationResult.NOT_FOUND);
                } else {
                    return movementsRepositoryPort.saveMovement(movement)
                        .flatMap(result -> {
                            if (result == OperationResult.OK) {
                                if (wallets.get(0).getId().equals(movement.getOriginWallet())) {
                                    return walletsRepositoryPort.updateWallet(wallets.get(0).setBalance(wallets.get(0).getBalance().subtract(movement.getAmount())))
                                        .then(walletsRepositoryPort.updateWallet(wallets.get(1).setBalance(wallets.get(1).getBalance().add(movement.getAmount()))));
                                } else {
                                    return walletsRepositoryPort.updateWallet(wallets.get(1).setBalance(wallets.get(1).getBalance().subtract(movement.getAmount())))
                                        .then(walletsRepositoryPort.updateWallet(wallets.get(0).setBalance(wallets.get(0).getBalance().add(movement.getAmount()))));
                                }
                            } else {
                                return Mono.just(result);
                            }
                        });
                }
            })
            .onErrorReturn(OperationResult.ERROR);
    }
}
