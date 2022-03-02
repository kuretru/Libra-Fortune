package com.kuretru.web.libra.entity.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@Schema(description = "账目与标签-查询条件")
public class LedgerEntityTagQuery {
    @Size(max = 64)
    @Schema(description = "账目ID")
    private String entityId;

    @Size(max = 64)
    @Schema(description = "标签ID")
    private String tagId;

}
