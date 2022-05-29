package com.angelperez.iobuilderswallets.infrastructureports;

import com.angelperez.iobuilderswallets.common.OperationResult;
import com.angelperez.iobuilderswallets.model.Wallet;
import reactor.core.publisher.Mono;

public interface WalletsRepositoryPort {

    Mono<Wallet> getWallet(String id);

    Mono<OperationResult> saveWallet(Wallet wallet);

    Mono<OperationResult> updateWallet(Wallet wallet);

    Mono<OperationResult> deleteWallet(String id);
}
