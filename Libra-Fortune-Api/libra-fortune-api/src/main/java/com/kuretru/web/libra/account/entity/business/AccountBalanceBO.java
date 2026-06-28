package com.kuretru.web.libra.account.entity.business;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountBalanceBO {

    @Schema(description = "账户ID")
    private Long accountId;

    @NotNull
    @Schema(description = "余额")
    private BigDecimal balance;

}
