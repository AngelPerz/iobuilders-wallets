package com.angelperez.iobuilderswallets.rest.controller;

import com.angelperez.iobuilderswallets.applicationports.DepositsService;
import com.angelperez.iobuilderswallets.common.OperationResult;
import com.angelperez.iobuilderswallets.model.Deposit;
import com.angelperez.iobuilderswallets.rest.dto.DepositWriteDTO;
import com.angelperez.iobuilderswallets.rest.mapper.DepositsMapperImpl;
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
public class DepositControllerTest {

    private DepositController depositController;

    private DepositsService depositsService;

    @BeforeEach
    void setUp() {
        depositsService = Mockito.mock(DepositsService.class);
        depositController = new DepositController(depositsService, new DepositsMapperImpl());
    }

    @Test
    public void createDeposit_onExistingWallet_returnsCreated() {
        Deposit deposit = new Deposit()
            .setWalletId("testId")
            .setAmount(BigDecimal.TEN);

        Mockito.when(depositsService.saveDeposit(deposit)).thenReturn(Mono.just(OperationResult.OK));

        Mono<ResponseEntity<Void>> result = depositController.createDeposit(new DepositWriteDTO()
            .setWalletId("testId")
            .setAmount(BigDecimal.TEN));

        assertThat(result.block())
            .isNotNull()
            .extracting(ResponseEntity::getStatusCode)
            .isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void createDeposit_onNotExistingWallet_returnsNotFound() {
        Deposit deposit = new Deposit()
            .setWalletId("testId")
            .setAmount(BigDecimal.TEN);

        Mockito.when(depositsService.saveDeposit(deposit)).thenReturn(Mono.just(OperationResult.NOT_FOUND));

        Mono<ResponseEntity<Void>> result = depositController.createDeposit(new DepositWriteDTO()
            .setWalletId("testId")
            .setAmount(BigDecimal.TEN));

        assertThat(result.block())
            .isNotNull()
            .extracting(ResponseEntity::getStatusCode)
            .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void createDeposit_onErrorSaving_returnsError() {
        Deposit deposit = new Deposit()
            .setWalletId("testId")
            .setAmount(BigDecimal.TEN);

        Mockito.when(depositsService.saveDeposit(deposit)).thenReturn(Mono.just(OperationResult.ERROR));

        Mono<ResponseEntity<Void>> result = depositController.createDeposit(new DepositWriteDTO()
            .setWalletId("testId")
            .setAmount(BigDecimal.TEN));

        assertThat(result.block())
            .isNotNull()
            .extracting(ResponseEntity::getStatusCode)
            .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
