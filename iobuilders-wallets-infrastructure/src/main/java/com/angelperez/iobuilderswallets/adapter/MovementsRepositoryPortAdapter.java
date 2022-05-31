package com.angelperez.iobuilderswallets.adapter;

import com.angelperez.iobuilderswallets.common.OperationResult;
import com.angelperez.iobuilderswallets.infrastructureports.MovementsRepositoryPort;
import com.angelperez.iobuilderswallets.mapper.MovementEntitiesMapper;
import com.angelperez.iobuilderswallets.model.Movement;
import com.angelperez.iobuilderswallets.r2dbc.repository.MovementsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@AllArgsConstructor
public class MovementsRepositoryPortAdapter implements MovementsRepositoryPort {

    private MovementsRepository movementsRepository;

    private MovementEntitiesMapper movementEntitiesMapper;

    @Override
    public Mono<OperationResult> saveMovement(Movement movement) {
        return movementsRepository.save(movementEntitiesMapper.toEntity(movement)
                .setNew(true)
                .setCreationTime(LocalDateTime.now()))
            .doOnError(err -> log.error(String.format("Error saving movement for origin_wallet [%s] and destiny_wallet [%s]",
                movement.getOriginWallet(), movement.getDestinyWallet()), err))
            .map(savedEntity -> OperationResult.OK)
            .onErrorReturn(OperationResult.ERROR);
    }

    @Override
    public Flux<Movement> getMovementsByWalletId(String walletId) {
        return movementsRepository.findAllByOriginWalletOrDestinyWalletOrderByCreationTimeDesc(walletId, walletId)
            .map(movementEntitiesMapper::toDomainModel);
    }
}
