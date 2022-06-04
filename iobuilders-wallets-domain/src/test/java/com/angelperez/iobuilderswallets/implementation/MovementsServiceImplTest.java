package com.angelperez.iobuilderswallets.implementation;

import com.angelperez.iobuilderswallets.common.OperationResult;
import com.angelperez.iobuilderswallets.infrastructureports.MovementsRepositoryPort;
import com.angelperez.iobuilderswallets.infrastructureports.WalletsRepositoryPort;
import com.angelperez.iobuilderswallets.model.Movement;
import com.angelperez.iobuilderswallets.model.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class MovementsServiceImplTest {

    private WalletsRepositoryPort walletsRepositoryPort;

    private MovementsRepositoryPort movementsRepositoryPort;

    private MovementsServiceImpl movementsService;

    @BeforeEach
    void setUp() {
        walletsRepositoryPort = Mockito.mock(WalletsRepositoryPort.class);
        movementsRepositoryPort = Mockito.mock(MovementsRepositoryPort.class);
        movementsService = new MovementsServiceImpl(walletsRepositoryPort, movementsRepositoryPort);
    }

    @Test
    public void saveMovement_onExistingWallet_callsToSaveTheMovementAndUpdatesWalletBalance() {
        Wallet wallet = new Wallet()
            .setId("testId")
            .setOwner("testOwner")
            .setAlias("testAlias")
            .setBalance(BigDecimal.TEN);

        Wallet wallet2 = new Wallet()
            .setId("testId2")
            .setOwner("testOwner")
            .setAlias("testAlias")
            .setBalance(BigDecimal.TEN);

        Movement movement = new Movement()
            .setOriginWallet("testId")
            .setDestinyWallet("testId2")
            .setAmount(BigDecimal.TEN);

        Mockito.when(walletsRepositoryPort.getWallet("testId")).thenReturn(Mono.just(wallet));
        Mockito.when(walletsRepositoryPort.getWallet("testId2")).thenReturn(Mono.just(wallet2));
        Mockito.when(movementsRepositoryPort.saveMovement(movement)).thenReturn(Mono.just(OperationResult.OK));
        Mockito.when(walletsRepositoryPort.updateWallet(Mockito.any())).thenReturn(Mono.just(OperationResult.OK));

        Mono<OperationResult> result = movementsService.saveMovement(movement);

        assertThat(result.block()).isEqualTo(OperationResult.OK);
        verify(walletsRepositoryPort, times(2)).updateWallet(Mockito.any());
    }

    @Test
    public void saveMovement_onNotExistingWallet_returnsNotFound() {
        Wallet wallet = new Wallet()
            .setId("testId")
            .setOwner("testOwner")
            .setAlias("testAlias")
            .setBalance(BigDecimal.TEN);

        Movement movement = new Movement()
            .setOriginWallet("testId")
            .setDestinyWallet("testId2")
            .setAmount(BigDecimal.TEN);

        Mockito.when(walletsRepositoryPort.getWallet("testId")).thenReturn(Mono.just(wallet));
        Mockito.when(walletsRepositoryPort.getWallet("testId2")).thenReturn(Mono.empty());

        Mono<OperationResult> result = movementsService.saveMovement(movement);

        assertThat(result.block()).isEqualTo(OperationResult.NOT_FOUND);
        verify(walletsRepositoryPort, never()).updateWallet(Mockito.any());
    }

    @Test
    public void saveMovement_onError_returnsError() {
        Wallet wallet = new Wallet()
            .setId("testId")
            .setOwner("testOwner")
            .setAlias("testAlias")
            .setBalance(BigDecimal.TEN);

        Movement movement = new Movement()
            .setOriginWallet("testId")
            .setDestinyWallet("testId2")
            .setAmount(BigDecimal.TEN);

        Mockito.when(walletsRepositoryPort.getWallet("testId")).thenReturn(Mono.error(new Exception("FAIL TEST")));
        Mockito.when(walletsRepositoryPort.getWallet("testId2")).thenReturn(Mono.just(wallet));

        Mono<OperationResult> result = movementsService.saveMovement(movement);

        assertThat(result.block()).isEqualTo(OperationResult.ERROR);
        verify(walletsRepositoryPort, never()).updateWallet(Mockito.any());
    }
}
