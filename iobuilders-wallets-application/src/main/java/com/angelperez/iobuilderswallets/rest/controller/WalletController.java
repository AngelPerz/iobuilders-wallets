package com.angelperez.iobuilderswallets.rest.controller;

import com.angelperez.iobuilderswallets.applicationports.WalletsService;
import com.angelperez.iobuilderswallets.rest.dto.WalletReadDTO;
import com.angelperez.iobuilderswallets.rest.dto.WalletWriteDTO;
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
public class WalletController {

    private WalletsService walletsService;

    private WalletsMapper walletsMapper;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Wallet retrieved for the given id"),
        @ApiResponse(responseCode = "404", description = "Wallet not found")})
    @GetMapping("/wallets/{id}")
    public Mono<ResponseEntity<WalletReadDTO>> getWalletById(@PathVariable(value = "id") String id) {
        return walletsService.getWallet(id)
            .map(t -> ResponseEntity.ok(walletsMapper.toDTO(t)))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Wallet created"),
        @ApiResponse(responseCode = "409", description = "Wallet already exists")})
    @PostMapping("/wallets")
    public Mono<ResponseEntity<Void>> createWallet(@RequestBody WalletWriteDTO walletDTO) {
        return walletsService.saveWallet(walletsMapper.toDomainModel(walletDTO))
            .map(res -> switch (res) {
                case OK -> new ResponseEntity<>(HttpStatus.CREATED);
                case CONFLICT -> new ResponseEntity<>(HttpStatus.CONFLICT);
                default -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            });
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Wallet updated"),
        @ApiResponse(responseCode = "404", description = "Wallet not found")})
    @PutMapping("/wallets")
    public Mono<ResponseEntity<Void>> updateWallet(@RequestBody WalletWriteDTO walletDTO) {
        return walletsService.updateWallet(walletsMapper.toDomainModel(walletDTO))
            .map(res -> switch (res) {
                case OK -> new ResponseEntity<>(HttpStatus.OK);
                case NOT_FOUND -> new ResponseEntity<>(HttpStatus.NOT_FOUND);
                default -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            });
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Wallet deleted"),
        @ApiResponse(responseCode = "404", description = "Wallet not found")})
    @DeleteMapping("/wallets/{id}")
    public Mono<ResponseEntity<Void>> deleteWallet(@PathVariable(value = "id") String id) {
        return walletsService.deleteWallet(id)
            .map(res -> switch (res) {
                case OK -> new ResponseEntity<>(HttpStatus.OK);
                case NOT_FOUND -> new ResponseEntity<>(HttpStatus.NOT_FOUND);
                default -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            });
    }
}
