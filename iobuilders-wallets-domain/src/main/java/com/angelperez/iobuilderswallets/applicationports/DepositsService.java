package com.angelperez.iobuilderswallets.applicationports;

import com.angelperez.iobuilderswallets.common.OperationResult;
import com.angelperez.iobuilderswallets.model.Deposit;
import reactor.core.publisher.Mono;

public interface DepositsService {

    Mono<OperationResult> saveDeposit(Deposit deposit);

}
