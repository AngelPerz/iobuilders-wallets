package com.angelperez.iobuilderswallets.r2dbc.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Table(name = "deposits")
@Accessors(chain = true)
public class DepositEntity implements Persistable<Long> {

    @Id
    private Long id;

    @Column("wallet_id")
    private String walletId;

    private BigDecimal amount;

    @Column("request_time")
    @CreatedDate
    private LocalDateTime requestTime;

    @Transient
    private boolean isNew;

    @Override
    public boolean isNew() {
        return isNew;
    }
}
