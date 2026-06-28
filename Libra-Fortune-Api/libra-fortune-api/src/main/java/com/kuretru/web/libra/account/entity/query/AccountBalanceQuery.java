package com.kuretru.web.libra.account.entity.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AccountBalanceQuery {

    @Schema(description = "开始日期")
    private LocalDate dateBegin;

    @Schema(description = "结束日期")
    private LocalDate dateEnd;

}
