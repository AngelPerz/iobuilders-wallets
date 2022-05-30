package com.angelperez.iobuilderswallets.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class DepositWriteDTO {

    private String walletId;

    private BigDecimal amount;

}
