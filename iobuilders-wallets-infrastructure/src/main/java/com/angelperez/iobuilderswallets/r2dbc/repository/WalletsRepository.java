package com.angelperez.iobuilderswallets.r2dbc.repository;

import com.angelperez.iobuilderswallets.r2dbc.entity.WalletEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletsRepository extends ReactiveCrudRepository<WalletEntity, String> {

}
