package com.angelperez.iobuilderswallets.service;

import com.angelperez.iobuilderswallets.common.OperationResult;
import com.angelperez.iobuilderswallets.infrastructureports.DepositsRepositoryPort;
import com.angelperez.iobuilderswallets.infrastructureports.WalletsRepositoryPort;
import com.angelperez.iobuilderswallets.model.Deposit;
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
public class DepositsServiceImplTest {

    private WalletsRepositoryPort walletsRepositoryPort;

    private DepositsRepositoryPort depositsRepositoryPort;

    private DepositsServiceImpl depositsService;

    @BeforeEach
    void setUp() {
        walletsRepositoryPort = Mockito.mock(WalletsRepositoryPort.class);
        depositsRepositoryPort = Mockito.mock(DepositsRepositoryPort.class);
        depositsService = new DepositsServiceImpl(walletsRepositoryPort, depositsRepositoryPort);
    }

    @Test
    public void saveDeposit_onExistingWallet_callsToSaveTheDepositAndUpdatesWalletBalance() {
        Wallet wallet = new Wallet()
            .setId("testId")
            .setOwner("testOwner")
            .setAlias("testAlias")
            .setBalance(BigDecimal.TEN);

        Deposit deposit = new Deposit()
            .setWalletId("testId")
            .setAmount(BigDecimal.TEN);

        Mockito.when(walletsRepositoryPort.getWallet("testId")).thenReturn(Mono.just(wallet));
        Mockito.when(depositsRepositoryPort.saveDeposit(deposit)).thenReturn(Mono.just(OperationResult.OK));
        Mockito.when(walletsRepositoryPort.updateWallet(Mockito.any())).thenReturn(Mono.just(OperationResult.OK));

        Mono<OperationResult> result = depositsService.saveDeposit(deposit);

        assertThat(result.block()).isEqualTo(OperationResult.OK);
        verify(walletsRepositoryPort, times(1)).updateWallet(Mockito.any());
    }

    @Test
    public void saveDeposit_onError_returnsError() {
        Deposit deposit = new Deposit()
            .setWalletId("testId")
            .setAmount(BigDecimal.TEN);

        Mockito.when(walletsRepositoryPort.getWallet("testId")).thenReturn(Mono.error(new Exception("FAIL TEST")));

        Mono<OperationResult> result = depositsService.saveDeposit(deposit);

        assertThat(result.block()).isEqualTo(OperationResult.ERROR);
        verify(walletsRepositoryPort, never()).updateWallet(Mockito.any());
    }
}
