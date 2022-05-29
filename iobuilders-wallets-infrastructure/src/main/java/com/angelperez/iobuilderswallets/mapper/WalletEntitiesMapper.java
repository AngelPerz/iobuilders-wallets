package com.angelperez.iobuilderswallets.mapper;

import com.angelperez.iobuilderswallets.model.Wallet;
import com.angelperez.iobuilderswallets.r2dbc.entity.WalletEntity;
import org.mapstruct.Mapper;

@Mapper
public interface WalletEntitiesMapper {

    WalletEntity toEntity(Wallet wallet);

    Wallet toDomainModel(WalletEntity walletEntity);

}
