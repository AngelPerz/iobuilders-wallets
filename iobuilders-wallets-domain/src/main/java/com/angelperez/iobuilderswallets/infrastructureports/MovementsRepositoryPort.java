package com.angelperez.iobuilderswallets.infrastructureports;

import com.angelperez.iobuilderswallets.common.OperationResult;
import com.angelperez.iobuilderswallets.model.Movement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovementsRepositoryPort {

    Mono<OperationResult> saveMovement(Movement movement);

    Flux<Movement> getMovementsByWalletId(String walletId);
}
