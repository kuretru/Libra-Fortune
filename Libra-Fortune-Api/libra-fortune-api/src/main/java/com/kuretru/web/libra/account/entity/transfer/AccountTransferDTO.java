package com.kuretru.web.libra.account.entity.transfer;

import com.kuretru.microservices.web.entity.transfer.BaseCreateUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AccountTransferDTO extends BaseCreateUpdateDTO {

    @Schema(description = "转账记录Owner", accessMode = Schema.AccessMode.READ_ONLY)
    private String owner;

    @NotNull
    @Schema(description = "转账日期")
    private LocalDate date;

    @NotEmpty
    @Size(min = 1, max = 32)
    @Schema(description = "转账名称")
    private String name;

    @NotNull
    @Positive
    @Schema(description = "转出账户ID")
    private Long sourceAccountId;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    @Digits(integer = 8, fraction = 2)
    @Schema(description = "转出金额")
    private BigDecimal sourceAmount;

    @NotEmpty
    @Size(min = 3, max = 3)
    @Schema(description = "转出货币")
    private String sourceCurrency;

    @NotNull
    @Positive
    @Schema(description = "转入账户ID")
    private Long targetAccountId;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    @Digits(integer = 8, fraction = 2)
    @Schema(description = "转入金额")
    private BigDecimal targetAmount;

    @NotEmpty
    @Size(min = 3, max = 3)
    @Schema(description = "转入货币")
    private String targetCurrency;

    @Schema(description = "备注")
    private String remark;

}
