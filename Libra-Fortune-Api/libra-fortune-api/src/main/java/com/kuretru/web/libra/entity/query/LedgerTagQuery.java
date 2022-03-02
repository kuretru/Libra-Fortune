package com.kuretru.web.libra.entity.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;


@Data
@Schema(description = "标签-查询条件")
public class LedgerTagQuery {
    @Size(max = 64)
    @Schema(description = "用户ID")
    private String userId;

    @Size(max = 16)
    @Schema(description = "标签名称")
    private String name;

}
