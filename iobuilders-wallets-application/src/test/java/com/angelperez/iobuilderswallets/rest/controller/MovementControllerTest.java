package com.angelperez.iobuilderswallets.rest.controller;

import com.angelperez.iobuilderswallets.applicationports.MovementsService;
import com.angelperez.iobuilderswallets.common.OperationResult;
import com.angelperez.iobuilderswallets.model.Movement;
import com.angelperez.iobuilderswallets.rest.dto.MovementWriteDTO;
import com.angelperez.iobuilderswallets.rest.mapper.MovementsMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class MovementControllerTest {

    private MovementController movementController;

    private MovementsService movementsService;

    @BeforeEach
    void setUp() {
        movementsService = Mockito.mock(MovementsService.class);
        movementController = new MovementController(movementsService, new MovementsMapperImpl());
    }

    @Test
    public void createMovement_onExistingWallet_returnsCreated() {
        Movement movement = new Movement()
            .setOriginWallet("testId1")
            .setDestinyWallet("testId2")
            .setAmount(BigDecimal.TEN);

        Mockito.when(movementsService.saveMovement(movement)).thenReturn(Mono.just(OperationResult.OK));

        Mono<ResponseEntity<Void>> result = movementController.createMovement(new MovementWriteDTO()
            .setOriginWallet("testId1")
            .setDestinyWallet("testId2")
            .setAmount(BigDecimal.TEN));

        assertThat(result.block())
            .isNotNull()
            .extracting(ResponseEntity::getStatusCode)
            .isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void createMovement_onNotExistingWallet_returnsNotFound() {
        Movement movement = new Movement()
            .setOriginWallet("testId1")
            .setDestinyWallet("testId2")
            .setAmount(BigDecimal.TEN);

        Mockito.when(movementsService.saveMovement(movement)).thenReturn(Mono.just(OperationResult.NOT_FOUND));

        Mono<ResponseEntity<Void>> result = movementController.createMovement(new MovementWriteDTO()
            .setOriginWallet("testId1")
            .setDestinyWallet("testId2")
            .setAmount(BigDecimal.TEN));

        assertThat(result.block())
            .isNotNull()
            .extracting(ResponseEntity::getStatusCode)
            .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void createMovement_onErrorSaving_returnsError() {
        Movement movement = new Movement()
            .setOriginWallet("testId1")
            .setDestinyWallet("testId2")
            .setAmount(BigDecimal.TEN);

        Mockito.when(movementsService.saveMovement(movement)).thenReturn(Mono.just(OperationResult.ERROR));

        Mono<ResponseEntity<Void>> result = movementController.createMovement(new MovementWriteDTO()
            .setOriginWallet("testId1")
            .setDestinyWallet("testId2")
            .setAmount(BigDecimal.TEN));

        assertThat(result.block())
            .isNotNull()
            .extracting(ResponseEntity::getStatusCode)
            .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
