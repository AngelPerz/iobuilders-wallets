package com.angelperez.iobuilderswallets.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Wallet {

    private String id;

    private String owner;

    private String alias;

    private BigDecimal balance;

}
