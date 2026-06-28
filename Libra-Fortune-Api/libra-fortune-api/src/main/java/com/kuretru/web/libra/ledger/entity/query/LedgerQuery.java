package com.kuretru.web.libra.ledger.entity.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LedgerQuery {

    @Schema(description = "账本名称，模糊查询")
    private String nameLike;

}
