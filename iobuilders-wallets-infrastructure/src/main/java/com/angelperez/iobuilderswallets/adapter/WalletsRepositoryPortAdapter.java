package com.angelperez.iobuilderswallets.adapter;

import com.angelperez.iobuilderswallets.common.OperationResult;
import com.angelperez.iobuilderswallets.infrastructureports.WalletsRepositoryPort;
import com.angelperez.iobuilderswallets.mapper.WalletEntitiesMapper;
import com.angelperez.iobuilderswallets.model.Wallet;
import com.angelperez.iobuilderswallets.r2dbc.entity.WalletEntity;
import com.angelperez.iobuilderswallets.r2dbc.repository.WalletsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
public class WalletsRepositoryPortAdapter implements WalletsRepositoryPort {

    private WalletsRepository walletsRepository;

    private WalletEntitiesMapper walletEntitiesMapper;

    @Override
    public Mono<Wallet> getWallet(String id) {
        return walletsRepository.findById(id).map(walletEntitiesMapper::toDomainModel);
    }

    @Override
    public Mono<OperationResult> saveWallet(Wallet wallet) {
        return walletsRepository.findById(wallet.getId())
            .hasElement()
            .flatMap(hasValue -> {
                if (hasValue) {
                    return Mono.just(OperationResult.CONFLICT);
                } else {
                    return walletsRepository.save(walletEntitiesMapper.toEntity(wallet).setNew(true))
                        .doOnError(err -> log.error(String.format("Error saving wallet with id [%s]", wallet.getId()), err))
                        .map(savedEntity -> OperationResult.OK)
                        .onErrorReturn(OperationResult.ERROR);
                }
            })
            .doOnError(err -> log.error(String.format("Error searching for wallet with id [%s]", wallet.getId()), err))
            .onErrorReturn(OperationResult.ERROR);
    }

    @Override
    public Mono<OperationResult> updateWallet(Wallet wallet) {
        return walletsRepository.findById(wallet.getId())
            .flatMap(entity -> walletsRepository.save(mergeWalletProperties(entity, wallet).setNew(false))
                .doOnError(err -> log.error(String.format("Error updating wallet with id [%s]", wallet.getId()), err))
                .map(updatedEntity -> OperationResult.OK)
                .onErrorReturn(OperationResult.ERROR))
            .defaultIfEmpty(OperationResult.NOT_FOUND)
            .doOnError(err -> log.error(String.format("Error searching for wallet with id [%s]", wallet.getId()), err))
            .onErrorReturn(OperationResult.ERROR);
    }

    @Override
    public Mono<OperationResult> deleteWallet(String id) {
        return walletsRepository.findById(id)
            .hasElement()
            .flatMap(hasValue -> {
                if (!hasValue) {
                    return Mono.just(OperationResult.NOT_FOUND);
                } else {
                    return walletsRepository.deleteById(id)
                        .doOnError(err -> log.error(String.format("Error deleting wallet with id [%s]", id), err))
                        .map(v -> OperationResult.OK)
                        .onErrorReturn(OperationResult.ERROR);
                }
            })
            .doOnError(err -> log.error(String.format("Error searching for wallet with id [%s]", id), err))
            .onErrorReturn(OperationResult.ERROR);
    }

    private WalletEntity mergeWalletProperties(WalletEntity walletEntity, Wallet wallet) {
        return walletEntity.setName(wallet.getName())
            .setSurname(wallet.getSurname())
            .setEmail(wallet.getEmail())
            .setPhone(wallet.getPhone());
    }
}
