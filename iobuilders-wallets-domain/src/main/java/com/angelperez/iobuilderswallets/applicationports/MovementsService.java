package com.angelperez.iobuilderswallets.applicationports;

import com.angelperez.iobuilderswallets.common.OperationResult;
import com.angelperez.iobuilderswallets.model.Movement;
import reactor.core.publisher.Mono;

public interface MovementsService {

    Mono<OperationResult> saveMovement(Movement movement);

}
