package com.angelperez.iobuilderswallets.infrastructureports;

import com.angelperez.iobuilderswallets.common.OperationResult;
import com.angelperez.iobuilderswallets.model.Deposit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DepositsRepositoryPort {

    Mono<OperationResult> saveDeposit(Deposit deposit);

    Flux<Deposit> getDepositsByWalletId(String walletId);
}
