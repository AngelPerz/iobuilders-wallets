package com.angelperez.iobuilderswallets.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class WalletBalanceDTO {

    private String id;

    private String owner;

    private String alias;

    private BigDecimal balance;

    private List<MovementReadDTO> movements;

    private List<DepositReadDTO> deposits;

}
