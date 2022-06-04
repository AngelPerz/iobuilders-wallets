package com.angelperez.iobuilderswallets.adapter;

import com.angelperez.iobuilderswallets.common.OperationResult;
import com.angelperez.iobuilderswallets.mapper.MovementEntitiesMapperImpl;
import com.angelperez.iobuilderswallets.model.Movement;
import com.angelperez.iobuilderswallets.model.Wallet;
import com.angelperez.iobuilderswallets.r2dbc.entity.MovementEntity;
import com.angelperez.iobuilderswallets.r2dbc.entity.WalletEntity;
import com.angelperez.iobuilderswallets.r2dbc.repository.MovementsRepository;
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
public class MovementsRepositoryPortAdapterTest {

    private MovementsRepository movementsRepository;

    private MovementsRepositoryPortAdapter movementsRepositoryPortAdapter;

    @BeforeEach
    void setUp() {
        movementsRepository = Mockito.mock(MovementsRepository.class);
        movementsRepositoryPortAdapter = new MovementsRepositoryPortAdapter(movementsRepository, new MovementEntitiesMapperImpl());
    }

    @Test
    public void getMovementsByWalletId_onExistingMovements_returnsTheMovements() {
        Mockito.when(movementsRepository.findAllByOriginWalletOrDestinyWalletOrderByCreationTimeDesc("testId", "testId"))
            .thenReturn(Flux.just(new MovementEntity()
                .setId(1L)
                .setOriginWallet("testOrigin")
                .setDestinyWallet("testDestiny")
                .setCreationTime(LocalDateTime.of(2022, 5, 9, 4, 2))
                .setAmount(BigDecimal.TEN)));

        Flux<Movement> result = movementsRepositoryPortAdapter.getMovementsByWalletId("testId");

        assertThat(result.collectList().block()).isEqualTo(List.of(new Movement()
            .setId(1L)
            .setOriginWallet("testOrigin")
            .setDestinyWallet("testDestiny")
            .setCreationTime(LocalDateTime.of(2022, 5, 9, 4, 2))
            .setAmount(BigDecimal.TEN)));
    }

    @Test
    public void getMovementsByWalletId_onEmptyMovements_returnsEmptyList() {
        Mockito.when(movementsRepository.findAllByOriginWalletOrDestinyWalletOrderByCreationTimeDesc("testId", "testId"))
            .thenReturn(Flux.empty());

        Flux<Movement> result = movementsRepositoryPortAdapter.getMovementsByWalletId("testId");

        assertThat(result.collectList().block()).isEqualTo(List.of());
    }

    @Test
    public void saveMovement_onNewMovement_returnsOK() {
        MovementEntity entity = new MovementEntity()
            .setId(1L)
            .setOriginWallet("testOrigin")
            .setDestinyWallet("testDestiny")
            .setCreationTime(LocalDateTime.of(2022, 5, 9, 4, 2))
            .setAmount(BigDecimal.TEN);

        Mockito.when(movementsRepository.save(Mockito.any())).thenReturn(Mono.just(entity));

        Mono<OperationResult> result = movementsRepositoryPortAdapter.saveMovement(new Movement()
            .setId(1L)
            .setOriginWallet("testOrigin")
            .setDestinyWallet("testDestiny")
            .setCreationTime(LocalDateTime.of(2022, 5, 9, 4, 2))
            .setAmount(BigDecimal.TEN));

        assertThat(result.block()).isEqualTo(OperationResult.OK);
    }

    @Test
    public void saveMovement_onErrorSavingMovement_returnsERROR() {
        Mockito.when(movementsRepository.save(Mockito.any()))
            .thenReturn(Mono.error(new DataIntegrityViolationException("ERROR TEST")));

        Mono<OperationResult> result = movementsRepositoryPortAdapter.saveMovement(new Movement()
            .setId(1L)
            .setOriginWallet("testOrigin")
            .setDestinyWallet("testDestiny")
            .setCreationTime(LocalDateTime.of(2022, 5, 9, 4, 2))
            .setAmount(BigDecimal.TEN));

        assertThat(result.block()).isEqualTo(OperationResult.ERROR);
    }
}
