package com.angelperez.iobuilderswallets.r2dbc.repository;

import com.angelperez.iobuilderswallets.r2dbc.entity.DepositEntity;
import com.angelperez.iobuilderswallets.r2dbc.entity.WalletEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface DepositsRepository extends ReactiveCrudRepository<DepositEntity, Long> {

    Flux<DepositEntity> findAllByWalletIdOrderByCreationTimeDesc(String walletId);
}
