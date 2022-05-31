package com.angelperez.iobuilderswallets.rest.controller;

import com.angelperez.iobuilderswallets.applicationports.MovementsService;
import com.angelperez.iobuilderswallets.rest.dto.MovementWriteDTO;
import com.angelperez.iobuilderswallets.rest.mapper.MovementsMapper;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
public class MovementController {

    private MovementsService movementsService;

    private MovementsMapper movementsMapper;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Movement created"),
        @ApiResponse(responseCode = "404", description = "Wallet related not found")})
    @PostMapping("/movements")
    public Mono<ResponseEntity<Void>> createMovement(@Valid @RequestBody MovementWriteDTO movementDTO) {
        return movementsService.saveMovement(movementsMapper.toDomainModel(movementDTO))
            .map(res -> switch (res) {
                case OK -> new ResponseEntity<>(HttpStatus.CREATED);
                case NOT_FOUND -> new ResponseEntity<>(HttpStatus.NOT_FOUND);
                default -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            });
    }
}
