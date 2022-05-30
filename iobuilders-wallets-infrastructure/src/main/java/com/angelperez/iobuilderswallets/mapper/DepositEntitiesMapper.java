package com.angelperez.iobuilderswallets.mapper;

import com.angelperez.iobuilderswallets.model.Deposit;
import com.angelperez.iobuilderswallets.r2dbc.entity.DepositEntity;
import org.mapstruct.Mapper;

@Mapper
public interface DepositEntitiesMapper {

    DepositEntity toEntity(Deposit deposit);

    Deposit toDomainModel(DepositEntity depositEntity);

}
