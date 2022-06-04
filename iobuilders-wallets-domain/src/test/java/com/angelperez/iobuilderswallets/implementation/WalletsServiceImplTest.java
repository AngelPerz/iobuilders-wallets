package com.angelperez.iobuilderswallets.implementation;

import com.angelperez.iobuilderswallets.common.OperationResult;
import com.angelperez.iobuilderswallets.infrastructureports.DepositsRepositoryPort;
import com.angelperez.iobuilderswallets.infrastructureports.MovementsRepositoryPort;
import com.angelperez.iobuilderswallets.infrastructureports.UsersRepositoryPort;
import com.angelperez.iobuilderswallets.infrastructureports.WalletsRepositoryPort;
import com.angelperez.iobuilderswallets.model.Balance;
import com.angelperez.iobuilderswallets.model.Deposit;
import com.angelperez.iobuilderswallets.model.Movement;
import com.angelperez.iobuilderswallets.model.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class WalletsServiceImplTest {

    private WalletsRepositoryPort walletsRepositoryPort;

    private UsersRepositoryPort usersRepositoryPort;

    private MovementsRepositoryPort movementsRepositoryPort;

    private DepositsRepositoryPort depositsRepositoryPort;

    private WalletsServiceImpl walletsService;

    @BeforeEach
    void setUp() {
        walletsRepositoryPort = Mockito.mock(WalletsRepositoryPort.class);
        usersRepositoryPort = Mockito.mock(UsersRepositoryPort.class);
        movementsRepositoryPort = Mockito.mock(MovementsRepositoryPort.class);
        depositsRepositoryPort = Mockito.mock(DepositsRepositoryPort.class);
        walletsService = new WalletsServiceImpl(walletsRepositoryPort, usersRepositoryPort,
            movementsRepositoryPort, depositsRepositoryPort);
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
    public void getWalletBalance_onExistingWallet_returnsTheBalance() {
        Wallet wallet = new Wallet()
            .setId("testId")
            .setOwner("testOwner")
            .setAlias("testAlias")
            .setBalance(BigDecimal.TEN);

        Movement movement = new Movement()
            .setId(1L)
            .setOriginWallet("originWallet")
            .setDestinyWallet("destinyWallet")
            .setAmount(BigDecimal.TEN);

        Deposit deposit = new Deposit()
            .setId(1L)
            .setWalletId("walletId")
            .setAmount(BigDecimal.TEN);

        Mockito.when(walletsRepositoryPort.getWallet("testId")).thenReturn(Mono.just(wallet));
        Mockito.when(movementsRepositoryPort.getMovementsByWalletId("testId")).thenReturn(Flux.just(movement));
        Mockito.when(depositsRepositoryPort.getDepositsByWalletId("testId")).thenReturn(Flux.just(deposit));

        Mono<Balance> result = walletsService.getWalletBalance("testId");

        assertThat(result.block()).isEqualTo(new Balance()
            .setId("testId")
            .setOwner("testOwner")
            .setAlias("testAlias")
            .setBalance(BigDecimal.TEN)
            .setMovements(List.of(movement))
            .setDeposits(List.of(deposit)));
    }

    @Test
    public void getWalletBalance_onNotExistingWallet_returnsEmpty() {
        Mockito.when(walletsRepositoryPort.getWallet("testId")).thenReturn(Mono.empty());
        Mockito.when(movementsRepositoryPort.getMovementsByWalletId("testId")).thenReturn(Flux.empty());
        Mockito.when(depositsRepositoryPort.getDepositsByWalletId("testId")).thenReturn(Flux.empty());

        Mono<Balance> result = walletsService.getWalletBalance("testId");

        assertThat(result.block()).isNull();
    }

    @Test
    public void saveWallet_onExistingOwner_callsToSaveTheWallet() {
        Wallet wallet = new Wallet()
            .setId("testId")
            .setOwner("testOwner")
            .setAlias("testAlias")
            .setBalance(BigDecimal.TEN);

        Mockito.when(walletsRepositoryPort.saveWallet(wallet)).thenReturn(Mono.just(OperationResult.OK));
        Mockito.when(usersRepositoryPort.existsUser("testOwner")).thenReturn(Mono.just(true));

        Mono<OperationResult> result = walletsService.saveWallet(wallet);

        assertThat(result.block()).isEqualTo(OperationResult.OK);
    }

    @Test
    public void saveWallet_onNotExistingOwner_returnsNotFound() {
        Wallet wallet = new Wallet()
            .setId("testId")
            .setOwner("testOwner")
            .setAlias("testAlias")
            .setBalance(BigDecimal.TEN);

        Mockito.when(usersRepositoryPort.existsUser("testOwner")).thenReturn(Mono.just(false));

        Mono<OperationResult> result = walletsService.saveWallet(wallet);

        assertThat(result.block()).isEqualTo(OperationResult.NOT_FOUND);
        verify(walletsRepositoryPort, never()).saveWallet(wallet);
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
