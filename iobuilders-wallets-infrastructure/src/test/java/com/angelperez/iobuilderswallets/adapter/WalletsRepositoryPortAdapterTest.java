package com.angelperez.iobuilderswallets.adapter;

import com.angelperez.iobuilderswallets.common.OperationResult;
import com.angelperez.iobuilderswallets.mapper.WalletEntitiesMapperImpl;
import com.angelperez.iobuilderswallets.model.Wallet;
import com.angelperez.iobuilderswallets.r2dbc.entity.WalletEntity;
import com.angelperez.iobuilderswallets.r2dbc.repository.WalletsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class WalletsRepositoryPortAdapterTest {

    private WalletsRepository walletsRepository;

    private WalletsRepositoryPortAdapter walletsRepositoryPortAdapter;

    @BeforeEach
    void setUp() {
        walletsRepository = Mockito.mock(WalletsRepository.class);
        walletsRepositoryPortAdapter = new WalletsRepositoryPortAdapter(walletsRepository, new WalletEntitiesMapperImpl());
    }

    @Test
    public void getWallet_onExistingWallet_returnsTheWallet() {
        Mockito.when(walletsRepository.findById("testId")).thenReturn(Mono.just(new WalletEntity()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname")));

        Mono<Wallet> result = walletsRepositoryPortAdapter.getWallet("testId");

        assertThat(result.block()).isEqualTo(new Wallet()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname"));
    }

    @Test
    public void getWallet_onNotExistingWallet_returnsEmpty() {
        Mockito.when(walletsRepository.findById("testId")).thenReturn(Mono.empty());

        Mono<Wallet> result = walletsRepositoryPortAdapter.getWallet("testId");

        assertThat(result.block()).isNull();
    }

    @Test
    public void saveWallet_onNewWallet_returnsOK() {
        WalletEntity entity = new WalletEntity()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname");

        Mockito.when(walletsRepository.findById("testId")).thenReturn(Mono.empty());
        Mockito.when(walletsRepository.save(Mockito.any())).thenReturn(Mono.just(entity));

        Mono<OperationResult> result = walletsRepositoryPortAdapter.saveWallet(new Wallet()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname"));

        assertThat(result.block()).isEqualTo(OperationResult.OK);
    }

    @Test
    public void saveWallet_onExistingWallet_returnsConflict() {
        WalletEntity entity = new WalletEntity()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname");

        Mockito.when(walletsRepository.findById("testId")).thenReturn(Mono.just(entity));

        Mono<OperationResult> result = walletsRepositoryPortAdapter.saveWallet(new Wallet()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname"));

        assertThat(result.block()).isEqualTo(OperationResult.CONFLICT);
    }

    @Test
    public void saveWallet_onErrorSaving_returnsError() {
        Mockito.when(walletsRepository.findById("testId"))
            .thenReturn(Mono.error(new DataIntegrityViolationException("ERROR TEST")));

        Mono<OperationResult> result = walletsRepositoryPortAdapter.saveWallet(new Wallet()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname"));

        assertThat(result.block()).isEqualTo(OperationResult.ERROR);
    }

    @Test
    public void saveWallet_onErrorSaving_returnsError2() {
        Mockito.when(walletsRepository.findById("testId")).thenReturn(Mono.empty());
        Mockito.when(walletsRepository.save(Mockito.any()))
            .thenReturn(Mono.error(new DataIntegrityViolationException("ERROR TEST")));

        Mono<OperationResult> result = walletsRepositoryPortAdapter.saveWallet(new Wallet()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname"));

        assertThat(result.block()).isEqualTo(OperationResult.ERROR);
    }

    @Test
    public void updateWallet_onExistingWallet_returnsOK() {
        WalletEntity entity = new WalletEntity()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname");

        Mockito.when(walletsRepository.findById("testId")).thenReturn(Mono.just(entity));
        Mockito.when(walletsRepository.save(Mockito.any())).thenReturn(Mono.just(entity));

        Mono<OperationResult> result = walletsRepositoryPortAdapter.updateWallet(new Wallet()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname"));

        assertThat(result.block()).isEqualTo(OperationResult.OK);
    }

    @Test
    public void updateWallet_onNewWallet_returnsNotFound() {
        Mockito.when(walletsRepository.findById("testId")).thenReturn(Mono.empty());

        Mono<OperationResult> result = walletsRepositoryPortAdapter.updateWallet(new Wallet()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname"));

        assertThat(result.block()).isEqualTo(OperationResult.NOT_FOUND);
    }

    @Test
    public void updateWallet_onErrorSaving_returnsError() {
        Mockito.when(walletsRepository.findById("testId"))
            .thenReturn(Mono.error(new DataIntegrityViolationException("ERROR TEST")));

        Mono<OperationResult> result = walletsRepositoryPortAdapter.updateWallet(new Wallet()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname"));

        assertThat(result.block()).isEqualTo(OperationResult.ERROR);
    }

    @Test
    public void updateWallet_onErrorSaving_returnsError2() {
        WalletEntity entity = new WalletEntity()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname");

        Mockito.when(walletsRepository.findById("testId")).thenReturn(Mono.just(entity));
        Mockito.when(walletsRepository.save(Mockito.any()))
            .thenReturn(Mono.error(new DataIntegrityViolationException("ERROR TEST")));

        Mono<OperationResult> result = walletsRepositoryPortAdapter.updateWallet(new Wallet()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname"));

        assertThat(result.block()).isEqualTo(OperationResult.ERROR);
    }

    @Test
    public void deleteWallet_onNewWallet_returnsNotFound() {
        Mockito.when(walletsRepository.findById("testId")).thenReturn(Mono.empty());

        Mono<OperationResult> result = walletsRepositoryPortAdapter.deleteWallet("testId");

        assertThat(result.block()).isEqualTo(OperationResult.NOT_FOUND);
    }

    @Test
    public void deleteWallet_onErrorDeleting_returnsError() {
        Mockito.when(walletsRepository.findById("testId"))
            .thenReturn(Mono.error(new DataIntegrityViolationException("ERROR TEST")));

        Mono<OperationResult> result = walletsRepositoryPortAdapter.deleteWallet("testId");

        assertThat(result.block()).isEqualTo(OperationResult.ERROR);
    }

    @Test
    public void deleteWallet_onErrorDeleting_returnsError2() {
        WalletEntity entity = new WalletEntity()
            .setId("testId")
            .setEmail("testEmail")
            .setPhone(666999666)
            .setName("testName")
            .setSurname("testSurname");

        Mockito.when(walletsRepository.findById("testId")).thenReturn(Mono.just(entity));
        Mockito.when(walletsRepository.deleteById("testId"))
            .thenReturn(Mono.error(new DataIntegrityViolationException("ERROR TEST")));

        Mono<OperationResult> result = walletsRepositoryPortAdapter.deleteWallet("testId");

        assertThat(result.block()).isEqualTo(OperationResult.ERROR);
    }
}
