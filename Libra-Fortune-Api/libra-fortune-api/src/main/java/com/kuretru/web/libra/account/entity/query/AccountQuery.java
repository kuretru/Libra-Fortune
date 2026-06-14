package com.kuretru.web.libra.account.entity.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AccountQuery {

    @Schema(description = "账户Owner")
    private String owner;

    @Schema(description = "账户名称，模糊查询")
    private String nameLike;

    @Schema(description = "该账户是否可以存储资金，精准查询")
    private Boolean canHoldFunds;

}
