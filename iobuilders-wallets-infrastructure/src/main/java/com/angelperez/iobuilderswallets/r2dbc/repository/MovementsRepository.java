package com.angelperez.iobuilderswallets.r2dbc.repository;

import com.angelperez.iobuilderswallets.r2dbc.entity.MovementEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MovementsRepository extends ReactiveCrudRepository<MovementEntity, Long> {

    Flux<MovementEntity> findAllByOriginWalletOrDestinyWalletOrderByCreationTimeDesc(String originWallet, String destinyWallet);
}
