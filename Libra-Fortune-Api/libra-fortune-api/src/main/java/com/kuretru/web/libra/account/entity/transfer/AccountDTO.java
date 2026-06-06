package com.kuretru.web.libra.account.entity.transfer;

import com.kuretru.microservices.web.v2.entity.transfer.BaseCreateUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AccountDTO extends BaseCreateUpdateDTO {

    @Schema(description = "账户Owner")
    private String owner;

    @Schema(description = "账户名称")
    private String name;

    @Schema(description = "图标")
    private String icon;

}
