package com.kuretru.web.libra.account.entity.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AccountBalanceQuery {

    @Schema(description = "账户ID")
    private Long accountId;

    @Schema(description = "账户ID列表")
    private List<Long> accountIdIn;

    @Schema(description = "日期")
    private LocalDate date;

}
