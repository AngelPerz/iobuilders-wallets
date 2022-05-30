package com.angelperez.iobuilderswallets.rest.controller;

import com.angelperez.iobuilderswallets.applicationports.DepositsService;
import com.angelperez.iobuilderswallets.applicationports.WalletsService;
import com.angelperez.iobuilderswallets.rest.dto.DepositWriteDTO;
import com.angelperez.iobuilderswallets.rest.dto.WalletReadDTO;
import com.angelperez.iobuilderswallets.rest.dto.WalletWriteDTO;
import com.angelperez.iobuilderswallets.rest.mapper.DepositsMapper;
import com.angelperez.iobuilderswallets.rest.mapper.WalletsMapper;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
public class DepositController {

    private DepositsService depositsService;

    private DepositsMapper depositsMapper;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Deposit created"),
        @ApiResponse(responseCode = "404", description = "Wallet related not found")})
    @PostMapping("/deposits")
    public Mono<ResponseEntity<Void>> createDeposit(@RequestBody DepositWriteDTO depositDTO) {
        return depositsService.saveDeposit(depositsMapper.toDomainModel(depositDTO))
            .map(res -> switch (res) {
                case OK -> new ResponseEntity<>(HttpStatus.CREATED);
                case NOT_FOUND -> new ResponseEntity<>(HttpStatus.NOT_FOUND);
                default -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            });
    }
}
