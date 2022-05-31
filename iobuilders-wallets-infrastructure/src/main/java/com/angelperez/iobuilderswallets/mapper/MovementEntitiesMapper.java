package com.angelperez.iobuilderswallets.mapper;

import com.angelperez.iobuilderswallets.model.Movement;
import com.angelperez.iobuilderswallets.r2dbc.entity.MovementEntity;
import org.mapstruct.Mapper;

@Mapper
public interface MovementEntitiesMapper {

    MovementEntity toEntity(Movement movement);

    Movement toDomainModel(MovementEntity movementEntity);

}
