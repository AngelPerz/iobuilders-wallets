package com.angelperez.iobuilderswallets.rest.controller;

import com.angelperez.iobuilderswallets.applicationports.WalletsService;
import com.angelperez.iobuilderswallets.common.OperationResult;
import com.angelperez.iobuilderswallets.model.Wallet;
import com.angelperez.iobuilderswallets.rest.dto.WalletDTO;
import com.angelperez.iobuilderswallets.rest.mapper.WalletsMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

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
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname")));

        Mono<ResponseEntity<WalletDTO>> result = walletController.getWalletById("testId");

        assertThat(result.block()).satisfies(
            res -> {
                assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(res.getBody()).isEqualTo(new WalletDTO()
                    .setId("testId")
                    .setEmail("testEmail")
                    .setPhone(666999666)
                    .setName("testName")
                    .setSurname("testSurname"));
            }
        );
    }

    @Test
    public void getWalletById_onNotExistingWallet_returnsNotFound() {
        Mockito.when(walletsService.getWallet("testId")).thenReturn(Mono.empty());

        Mono<ResponseEntity<WalletDTO>> result = walletController.getWalletById("testId");

        assertThat(result.block())
            .isNotNull()
            .extracting(ResponseEntity::getStatusCode)
            .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void createWallet_onNewWallet_returnsCreated() {
        Wallet wallet = new Wallet()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname");

        Mockito.when(walletsService.saveWallet(wallet)).thenReturn(Mono.just(OperationResult.OK));

        Mono<ResponseEntity<Void>> result = walletController.createWallet(new WalletDTO()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname"));

        assertThat(result.block())
            .isNotNull()
            .extracting(ResponseEntity::getStatusCode)
            .isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void createWallet_onExistingWallet_returnsConflict() {
        Wallet wallet = new Wallet()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname");

        Mockito.when(walletsService.saveWallet(wallet)).thenReturn(Mono.just(OperationResult.CONFLICT));

        Mono<ResponseEntity<Void>> result = walletController.createWallet(new WalletDTO()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname"));

        assertThat(result.block())
            .isNotNull()
            .extracting(ResponseEntity::getStatusCode)
            .isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void createWallet_onErrorAtSaving_returnsError() {
        Wallet wallet = new Wallet()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname");

        Mockito.when(walletsService.saveWallet(wallet)).thenReturn(Mono.just(OperationResult.ERROR));

        Mono<ResponseEntity<Void>> result = walletController.createWallet(new WalletDTO()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname"));

        assertThat(result.block())
            .isNotNull()
            .extracting(ResponseEntity::getStatusCode)
            .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void updateWallet_onExistingWallet_returnsOk() {
        Wallet wallet = new Wallet()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname");

        Mockito.when(walletsService.updateWallet(wallet)).thenReturn(Mono.just(OperationResult.OK));

        Mono<ResponseEntity<Void>> result = walletController.updateWallet(new WalletDTO()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname"));

        assertThat(result.block())
            .isNotNull()
            .extracting(ResponseEntity::getStatusCode)
            .isEqualTo(HttpStatus.OK);
    }

    @Test
    public void updateWallet_onNewWallet_returnsNotFound() {
        Wallet wallet = new Wallet()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname");

        Mockito.when(walletsService.updateWallet(wallet)).thenReturn(Mono.just(OperationResult.NOT_FOUND));

        Mono<ResponseEntity<Void>> result = walletController.updateWallet(new WalletDTO()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname"));

        assertThat(result.block())
            .isNotNull()
            .extracting(ResponseEntity::getStatusCode)
            .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateWallet_onErrorAtUpdating_returnsError() {
        Wallet wallet = new Wallet()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname");

        Mockito.when(walletsService.updateWallet(wallet)).thenReturn(Mono.just(OperationResult.ERROR));

        Mono<ResponseEntity<Void>> result = walletController.updateWallet(new WalletDTO()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname"));

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
