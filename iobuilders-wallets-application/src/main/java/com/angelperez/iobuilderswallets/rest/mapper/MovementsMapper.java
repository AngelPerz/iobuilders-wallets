package com.angelperez.iobuilderswallets.rest.mapper;

import com.angelperez.iobuilderswallets.model.Movement;
import com.angelperez.iobuilderswallets.rest.dto.MovementReadDTO;
import com.angelperez.iobuilderswallets.rest.dto.MovementWriteDTO;
import org.mapstruct.Mapper;

@Mapper
public interface MovementsMapper {

    MovementReadDTO toDTO(Movement movement);

    Movement toDomainModel(MovementWriteDTO movement);

}
