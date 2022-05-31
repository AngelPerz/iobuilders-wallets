package com.angelperez.iobuilderswallets.adapter;

import com.angelperez.iobuilderswallets.infrastructureports.UsersRepositoryPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
public class UsersRepositoryPortAdapter implements UsersRepositoryPort {

    private WebClient webClient;

    @Override
    public Mono<Boolean> existsUser(String ownerId) {
        return webClient.get()
            .uri(String.format("/%s", ownerId))
            .accept(MediaType.APPLICATION_JSON)
            .exchangeToMono(response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    return Mono.just(true);
                } else if (response.statusCode() == HttpStatus.NOT_FOUND) {
                    return Mono.just(false);
                } else {
                    log.error("Error getting user for id [{}], received status code was [{}]", ownerId, response.statusCode());
                    return Mono.just(false);
                }
            })
            .onErrorResume(err -> {
                log.error("Error performing a call to retrieve the user with id [{}]", ownerId);
                return Mono.just(false);
            });
    }
}
