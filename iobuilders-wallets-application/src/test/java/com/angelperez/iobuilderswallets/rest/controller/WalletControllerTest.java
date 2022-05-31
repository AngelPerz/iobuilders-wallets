package com.angelperez.iobuilderswallets.rest.controller;

import com.angelperez.iobuilderswallets.applicationports.WalletsService;
import com.angelperez.iobuilderswallets.common.OperationResult;
import com.angelperez.iobuilderswallets.model.Balance;
import com.angelperez.iobuilderswallets.model.Wallet;
import com.angelperez.iobuilderswallets.rest.dto.WalletBalanceDTO;
import com.angelperez.iobuilderswallets.rest.dto.WalletReadDTO;
import com.angelperez.iobuilderswallets.rest.dto.WalletWriteDTO;
import com.angelperez.iobuilderswallets.rest.mapper.WalletsMapperImpl;
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
public class WalletControllerTest {

    private WalletController walletController;

    private WalletsService walletsService;

    @BeforeEach
    void setUp() {
        walletsService = Mockito.mock(WalletsService.class);
        walletController = new WalletController(walletsService, new WalletsMapperImpl());
    }

    @Test
    public void getWalletById_onExistingWallet_returnsTheWallet() {
        Mockito.when(walletsService.getWallet("testId")).thenReturn(Mono.just(new Wallet()
            .setId("testId")
            .setOwner("testOwner")
            .setAlias("testAlias")
            .setBalance(BigDecimal.TEN)));

        Mono<ResponseEntity<WalletReadDTO>> result = walletController.getWalletById("testId");

        assertThat(result.block()).satisfies(
            res -> {
                assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(res.getBody()).isEqualTo(new WalletReadDTO()
                    .setId("testId")
                    .setOwner("testOwner")
                    .setAlias("testAlias")
                    .setBalance(BigDecimal.TEN));
            }
        );
    }

    @Test
    public void getWalletById_onNotExistingWallet_returnsNotFound() {
        Mockito.when(walletsService.getWallet("testId")).thenReturn(Mono.empty());

        Mono<ResponseEntity<WalletReadDTO>> result = walletController.getWalletById("testId");

        assertThat(result.block())
            .isNotNull()
            .extracting(ResponseEntity::getStatusCode)
            .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getWalletBalance_onExistingWallet_returnsTheWallet() {
        Mockito.when(walletsService.getWalletBalance("testId")).thenReturn(Mono.just(new Balance()
            .setId("testId")
            .setOwner("testOwner")
            .setAlias("testAlias")
            .setBalance(BigDecimal.TEN)));

        Mono<ResponseEntity<WalletBalanceDTO>> result = walletController.getWalletBalance("testId");

        assertThat(result.block()).satisfies(
            res -> {
                assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(res.getBody()).isEqualTo(new WalletBalanceDTO()
                    .setId("testId")
                    .setOwner("testOwner")
                    .setAlias("testAlias")
                    .setBalance(BigDecimal.TEN));
            }
        );
    }

    @Test
    public void getWalletBalance_onNotExistingWallet_returnsNotFound() {
        Mockito.when(walletsService.getWalletBalance("testId")).thenReturn(Mono.empty());

        Mono<ResponseEntity<WalletBalanceDTO>> result = walletController.getWalletBalance("testId");

        assertThat(result.block())
            .isNotNull()
            .extracting(ResponseEntity::getStatusCode)
            .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void createWallet_onNewWallet_returnsCreated() {
        Wallet wallet = new Wallet()
            .setId("testId")
            .setOwner("testOwner")
            .setAlias("testAlias");

        Mockito.when(walletsService.saveWallet(wallet)).thenReturn(Mono.just(OperationResult.OK));

        Mono<ResponseEntity<Void>> result = walletController.createWallet(new WalletWriteDTO()
            .setId("testId")
            .setOwner("testOwner")
            .setAlias("testAlias"));

        assertThat(result.block())
            .isNotNull()
            .extracting(ResponseEntity::getStatusCode)
            .isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void createWallet_onExistingWallet_returnsConflict() {
        Wallet wallet = new Wallet()
            .setId("testId")
            .setOwner("testOwner")
            .setAlias("testAlias");

        Mockito.when(walletsService.saveWallet(wallet)).thenReturn(Mono.just(OperationResult.CONFLICT));

        Mono<ResponseEntity<Void>> result = walletController.createWallet(new WalletWriteDTO()
            .setId("testId")
            .setOwner("testOwner")
            .setAlias("testAlias"));

        assertThat(result.block())
            .isNotNull()
            .extracting(ResponseEntity::getStatusCode)
            .isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void createWallet_onErrorAtSaving_returnsError() {
        Wallet wallet = new Wallet()
            .setId("testId")
            .setOwner("testOwner")
            .setAlias("testAlias");

        Mockito.when(walletsService.saveWallet(wallet)).thenReturn(Mono.just(OperationResult.ERROR));

        Mono<ResponseEntity<Void>> result = walletController.createWallet(new WalletWriteDTO()
            .setId("testId")
            .setOwner("testOwner")
            .setAlias("testAlias"));

        assertThat(result.block())
            .isNotNull()
            .extracting(ResponseEntity::getStatusCode)
            .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void updateWallet_onExistingWallet_returnsOk() {
        Wallet wallet = new Wallet()
            .setId("testId")
            .setOwner("testOwner")
            .setAlias("testAlias");

        Mockito.when(walletsService.updateWallet(wallet)).thenReturn(Mono.just(OperationResult.OK));

        Mono<ResponseEntity<Void>> result = walletController.updateWallet(new WalletWriteDTO()
            .setId("testId")
            .setOwner("testOwner")
            .setAlias("testAlias"));

        assertThat(result.block())
            .isNotNull()
            .extracting(ResponseEntity::getStatusCode)
            .isEqualTo(HttpStatus.OK);
    }

    @Test
    public void updateWallet_onNewWallet_returnsNotFound() {
        Wallet wallet = new Wallet()
            .setId("testId")
            .setOwner("testOwner")
            .setAlias("testAlias");

        Mockito.when(walletsService.updateWallet(wallet)).thenReturn(Mono.just(OperationResult.NOT_FOUND));

        Mono<ResponseEntity<Void>> result = walletController.updateWallet(new WalletWriteDTO()
            .setId("testId")
            .setOwner("testOwner")
            .setAlias("testAlias"));

        assertThat(result.block())
            .isNotNull()
            .extracting(ResponseEntity::getStatusCode)
            .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateWallet_onErrorAtUpdating_returnsError() {
        Wallet wallet = new Wallet()
            .setId("testId")
            .setOwner("testOwner")
            .setAlias("testAlias");

        Mockito.when(walletsService.updateWallet(wallet)).thenReturn(Mono.just(OperationResult.ERROR));

        Mono<ResponseEntity<Void>> result = walletController.updateWallet(new WalletWriteDTO()
            .setId("testId")
            .setOwner("testOwner")
            .setAlias("testAlias"));

        assertThat(result.block())
            .isNotNull()
            .extracting(ResponseEntity::getStatusCode)
            .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void deleteWallet_onExistingWallet_returnsOk() {
        Mockito.when(walletsService.deleteWallet("testId")).thenReturn(Mono.just(OperationResult.OK));

        Mono<ResponseEntity<Void>> result = walletController.deleteWallet("testId");

        assertThat(result.block())
            .isNotNull()
            .extracting(ResponseEntity::getStatusCode)
            .isEqualTo(HttpStatus.OK);
    }

    @Test
    public void deleteWallet_onNewWallet_returnsNotFound() {
        Mockito.when(walletsService.deleteWallet("testId")).thenReturn(Mono.just(OperationResult.NOT_FOUND));

        Mono<ResponseEntity<Void>> result = walletController.deleteWallet("testId");

        assertThat(result.block())
            .isNotNull()
            .extracting(ResponseEntity::getStatusCode)
            .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteWallet_onErrorAtUpdating_returnsError() {
        Mockito.when(walletsService.deleteWallet("testId")).thenReturn(Mono.just(OperationResult.ERROR));

        Mono<ResponseEntity<Void>> result = walletController.deleteWallet("testId");

        assertThat(result.block())
            .isNotNull()
            .extracting(ResponseEntity::getStatusCode)
            .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
