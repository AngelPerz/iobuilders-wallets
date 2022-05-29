package com.angelperez.iobuilderswallets.rest.mapper;

import com.angelperez.iobuilderswallets.model.Wallet;
import com.angelperez.iobuilderswallets.rest.dto.WalletDTO;
import org.mapstruct.Mapper;

@Mapper
public interface WalletsMapper {

    WalletDTO toDTO(Wallet wallet);

    Wallet toDomainModel(WalletDTO walletDTO);

}
