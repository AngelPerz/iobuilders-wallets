package com.angelperez.iobuilderswallets.rest.mapper;

import com.angelperez.iobuilderswallets.model.Wallet;
import com.angelperez.iobuilderswallets.rest.dto.WalletReadDTO;
import com.angelperez.iobuilderswallets.rest.dto.WalletWriteDTO;
import org.mapstruct.Mapper;

@Mapper
public interface WalletsMapper {

    WalletReadDTO toDTO(Wallet wallet);

    Wallet toDomainModel(WalletWriteDTO walletDTO);

}
