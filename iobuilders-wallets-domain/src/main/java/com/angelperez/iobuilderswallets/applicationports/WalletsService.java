package com.angelperez.iobuilderswallets.applicationports;

import com.angelperez.iobuilderswallets.common.OperationResult;
import com.angelperez.iobuilderswallets.model.Balance;
import com.angelperez.iobuilderswallets.model.Wallet;
import reactor.core.publisher.Mono;

public interface WalletsService {

    Mono<Wallet> getWallet(String id);

    Mono<Balance> getWalletBalance(String id);

    Mono<OperationResult> saveWallet(Wallet wallet);

    Mono<OperationResult> updateWallet(Wallet wallet);

    Mono<OperationResult> deleteWallet(String id);
}
