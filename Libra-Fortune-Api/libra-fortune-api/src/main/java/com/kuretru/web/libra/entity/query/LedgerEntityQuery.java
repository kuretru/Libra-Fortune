package com.kuretru.web.libra.entity.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;
import java.time.Instant;


@Data
@Schema(description = "账目-查询条件")
public class LedgerEntityQuery {
    @Size(max = 64)
    @Schema(description = "账本ID")
    private String ledgerId;

    @Size(max = 64)
    @Schema(description = "大分类ID")
    private String categoryId;

    @Schema(description = "发生时间")
    private String date;

    @Schema(description = "费用")
    private Long amount;

}
