package com.kuretru.web.libra.account.entity.business;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AccountBalanceDateBO {

    @Schema(description = "日期，表达截止当前的余额")
    private LocalDate date;

    @Schema(description = "账户余额")
    private List<AccountBalanceBO> items;

}
