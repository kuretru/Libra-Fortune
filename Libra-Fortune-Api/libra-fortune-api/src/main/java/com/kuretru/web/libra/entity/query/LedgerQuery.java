package com.kuretru.web.libra.entity.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@Schema(description = "账本-查询条件")
public class LedgerQuery {

    @Schema(description = "所属用户ID")
    private String userId;

    @Size(max = 16)
    @Schema(description = "账本名称")
    private String name;
}
