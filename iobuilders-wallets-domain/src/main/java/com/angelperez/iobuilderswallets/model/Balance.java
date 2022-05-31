package com.angelperez.iobuilderswallets.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Balance {

    private String id;

    private String owner;

    private String alias;

    private BigDecimal balance;

    private List<Movement> movements;

    private List<Deposit> deposits;
}
