package com.angelperez.iobuilderswallets.infrastructureports;

import reactor.core.publisher.Mono;

public interface UsersRepositoryPort {

    Mono<Boolean> existsUser(String ownerId);

}
