package com.angelperez.iobuilderswallets.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class DepositDTO {

    private Long id;

    private String walletId;

    private BigDecimal amount;

    private LocalDateTime requestTime;

}
