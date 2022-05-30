package com.angelperez.iobuilderswallets.rest.mapper;

import com.angelperez.iobuilderswallets.model.Deposit;
import com.angelperez.iobuilderswallets.model.Wallet;
import com.angelperez.iobuilderswallets.rest.dto.DepositReadDTO;
import com.angelperez.iobuilderswallets.rest.dto.DepositWriteDTO;
import com.angelperez.iobuilderswallets.rest.dto.WalletReadDTO;
import com.angelperez.iobuilderswallets.rest.dto.WalletWriteDTO;
import org.mapstruct.Mapper;

@Mapper
public interface DepositsMapper {

    DepositReadDTO toDTO(Deposit deposit);

    Deposit toDomainModel(DepositWriteDTO deposit);

}
