package com.kuretru.web.libra.entity.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.UUID;


@Data
@Schema(description = "账本许可-查询条件")
public class LedgerPermissionQuery {

    @Size(max = 64)
    @Schema(description = "用户ID")
    private String userId;

    @Size(max = 64)
    @Schema(description = "账本ID")
    private String ledgerId;

    @Size(max = 1)
    @Schema(description = "可读")
    private Integer readable;

    @Size(max = 1)
    @Schema(description = "可改")
    private Integer writable;
}
