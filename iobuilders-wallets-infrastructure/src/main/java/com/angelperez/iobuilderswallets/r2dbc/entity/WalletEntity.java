package com.angelperez.iobuilderswallets.r2dbc.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table(name = "wallets")
@Accessors(chain = true)
public class WalletEntity implements Persistable<String> {

    @Id
    private String id;

    private String name;

    private String surname;

    private String email;

    private Integer phone;

    @Transient
    private boolean isNew;

    @Override
    public boolean isNew() {
        return isNew;
    }
}
