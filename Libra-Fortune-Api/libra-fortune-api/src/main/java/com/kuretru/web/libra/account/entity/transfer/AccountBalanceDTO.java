package com.kuretru.web.libra.account.entity.transfer;

import com.kuretru.microservices.web.v2.entity.transfer.BaseCreateUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AccountBalanceDTO extends BaseCreateUpdateDTO {

    @Schema(description = "账户ID")
    private Long accountId;

    @Schema(description = "日期，表达截止当前的余额")
    private LocalDate date;

    @Schema(description = "余额")
    private BigDecimal balance;

}
