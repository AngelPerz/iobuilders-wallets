package com.angelperez.iobuilderswallets.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class DepositWriteDTO {

    @NotNull
    private String walletId;

    @Min(value = 0, message = "Amount must be positive")
    private BigDecimal amount;

}
