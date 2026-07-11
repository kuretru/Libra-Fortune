package com.kuretru.web.libra.account.entity.business;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AccountBalanceRequest {

    @Schema(description = "日期")
    private LocalDate date;

    @Schema(description = "By日余额列表")
    private List<AccountBalanceBO> balances;

}
