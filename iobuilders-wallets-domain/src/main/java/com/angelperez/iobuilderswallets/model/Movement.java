package com.angelperez.iobuilderswallets.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Movement {

    private Long id;

    private String originWallet;

    private String destinyWallet;

    private BigDecimal amount;

    private LocalDateTime requestTime;

}
