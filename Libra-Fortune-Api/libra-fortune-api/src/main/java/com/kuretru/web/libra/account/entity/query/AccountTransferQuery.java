package com.kuretru.web.libra.account.entity.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class AccountTransferQuery {

    @Schema(hidden = true)
    private String owner;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "转账日期开始")
    private LocalDate dateBegin;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "转账日期结束")
    private LocalDate dateEnd;

    @Schema(description = "转账名称，模糊查询")
    private String nameLike;

    @Schema(description = "转出账户ID")
    private Long sourceAccountId;

    @Schema(description = "转出货币")
    private String sourceCurrency;

    @Schema(description = "转入账户ID")
    private Long targetAccountId;

    @Schema(description = "转入货币")
    private String targetCurrency;

}
