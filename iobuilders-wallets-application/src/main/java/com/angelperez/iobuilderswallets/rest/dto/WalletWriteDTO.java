package com.angelperez.iobuilderswallets.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class WalletWriteDTO {

    private String id;

    private String owner;

    private String alias;

}
