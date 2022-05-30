package com.angelperez.iobuilderswallets.service;

import com.angelperez.iobuilderswallets.common.OperationResult;
import com.angelperez.iobuilderswallets.infrastructureports.WalletsRepositoryPort;
import com.angelperez.iobuilderswallets.model.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class WalletsServiceImplTest {

    private WalletsRepositoryPort walletsRepositoryPort;

    private WalletsServiceImpl walletsService;

    @BeforeEach
    void setUp() {
        walletsRepositoryPort = Mockito.mock(WalletsRepositoryPort.class);
        walletsService = new WalletsServiceImpl(walletsRepositoryPort);
    }

    @Test
    public void getWallet_onAnyCall_returnsTheRepositoryPortResult() {
        Wallet wallet = new Wallet()
            .setId("testId")
            .setOwner("testOwner")
            .setAlias("testAlias")
            .setBalance(BigDecimal.TEN);

        Mockito.when(walletsRepositoryPort.getWallet("testId")).thenReturn(Mono.just(wallet));

        Mono<Wallet> result = walletsService.getWallet("testId");

        assertThat(result.block()).isEqualTo(wallet);
    }

    @Test
    public void saveWallet_onAnyCall_returnsTheRepositoryPortResult() {
        Wallet wallet = new Wallet()
            .setId("testId")
            .setOwner("testOwner")
            .setAlias("testAlias")
            .setBalance(BigDecimal.TEN);

        Mockito.when(walletsRepositoryPort.saveWallet(wallet)).thenReturn(Mono.just(OperationResult.OK));

        Mono<OperationResult> result = walletsService.saveWallet(wallet);

        assertThat(result.block()).isEqualTo(OperationResult.OK);
    }

    @Test
    public void updateWallet_onAnyCall_returnsTheRepositoryPortResult() {
        Wallet wallet = new Wallet()
            .setId("testId")
            .setOwner("testOwner")
            .setAlias("testAlias")
            .setBalance(BigDecimal.TEN);

        Mockito.when(walletsRepositoryPort.updateWallet(wallet)).thenReturn(Mono.just(OperationResult.OK));

        Mono<OperationResult> result = walletsService.updateWallet(wallet);

        assertThat(result.block()).isEqualTo(OperationResult.OK);
    }

    @Test
    public void deleteWallet_onAnyCall_returnsTheRepositoryPortResult() {
        Mockito.when(walletsRepositoryPort.deleteWallet("testId")).thenReturn(Mono.just(OperationResult.OK));

        Mono<OperationResult> result = walletsService.deleteWallet("testId");

        assertThat(result.block()).isEqualTo(OperationResult.OK);
    }
}
