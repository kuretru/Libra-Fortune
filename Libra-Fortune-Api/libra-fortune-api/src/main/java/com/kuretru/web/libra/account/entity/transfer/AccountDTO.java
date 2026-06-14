package com.kuretru.web.libra.account.entity.transfer;

import com.kuretru.microservices.web.v2.entity.transfer.BaseCreateUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AccountDTO extends BaseCreateUpdateDTO {

    @Schema(description = "账户Owner")
    private String owner;

    @NotEmpty
    @Size(min = 1, max = 16)
    @Schema(description = "账户名称")
    private String name;

    @NotNull
    @Schema(description = "该账户是否可以存储资金")
    private Boolean canHoldFunds;

    @NotEmpty
    @Size(min = 1, max = 32)
    @Schema(description = "图标")
    private String icon;

}
