package com.angelperez.iobuilderswallets.adapter;

import com.angelperez.iobuilderswallets.common.OperationResult;
import com.angelperez.iobuilderswallets.mapper.DepositEntitiesMapperImpl;
import com.angelperez.iobuilderswallets.model.Deposit;
import com.angelperez.iobuilderswallets.r2dbc.entity.DepositEntity;
import com.angelperez.iobuilderswallets.r2dbc.repository.DepositsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class DepositsRepositoryPortAdapterTest {

    private DepositsRepository depositsRepository;

    private DepositsRepositoryPortAdapter depositsRepositoryPortAdapter;

    @BeforeEach
    void setUp() {
        depositsRepository = Mockito.mock(DepositsRepository.class);
        depositsRepositoryPortAdapter = new DepositsRepositoryPortAdapter(depositsRepository, new DepositEntitiesMapperImpl());
    }

    @Test
    public void getDepositsByWalletId_onExistingDeposits_returnsTheDeposits() {
        Mockito.when(depositsRepository.findAllByWalletIdOrderByCreationTimeDesc("testId"))
            .thenReturn(Flux.just(new DepositEntity()
                .setId(1L)
                .setWalletId("testOrigin")
                .setCreationTime(LocalDateTime.of(2022, 5, 9, 4, 2))
                .setAmount(BigDecimal.TEN)));

        Flux<Deposit> result = depositsRepositoryPortAdapter.getDepositsByWalletId("testId");

        assertThat(result.collectList().block()).isEqualTo(List.of(new Deposit()
            .setId(1L)
            .setWalletId("testOrigin")
            .setCreationTime(LocalDateTime.of(2022, 5, 9, 4, 2))
            .setAmount(BigDecimal.TEN)));
    }

    @Test
    public void getDepositsByWalletId_onEmptyDeposits_returnsEmptyList() {
        Mockito.when(depositsRepository.findAllByWalletIdOrderByCreationTimeDesc("testId"))
            .thenReturn(Flux.empty());

        Flux<Deposit> result = depositsRepositoryPortAdapter.getDepositsByWalletId("testId");

        assertThat(result.collectList().block()).isEqualTo(List.of());
    }

    @Test
    public void saveDeposit_onNewDeposit_returnsOK() {
        DepositEntity entity = new DepositEntity()
            .setId(1L)
            .setWalletId("testOrigin")
            .setCreationTime(LocalDateTime.of(2022, 5, 9, 4, 2))
            .setAmount(BigDecimal.TEN);

        Mockito.when(depositsRepository.save(Mockito.any())).thenReturn(Mono.just(entity));

        Mono<OperationResult> result = depositsRepositoryPortAdapter.saveDeposit(new Deposit()
            .setId(1L)
            .setWalletId("testOrigin")
            .setCreationTime(LocalDateTime.of(2022, 5, 9, 4, 2))
            .setAmount(BigDecimal.TEN));

        assertThat(result.block()).isEqualTo(OperationResult.OK);
    }

    @Test
    public void saveDeposit_onErrorSavingDeposit_returnsERROR() {
        Mockito.when(depositsRepository.save(Mockito.any()))
            .thenReturn(Mono.error(new DataIntegrityViolationException("ERROR TEST")));

        Mono<OperationResult> result = depositsRepositoryPortAdapter.saveDeposit(new Deposit()
            .setId(1L)
            .setWalletId("testOrigin")
            .setCreationTime(LocalDateTime.of(2022, 5, 9, 4, 2))
            .setAmount(BigDecimal.TEN));

        assertThat(result.block()).isEqualTo(OperationResult.ERROR);
    }
}
