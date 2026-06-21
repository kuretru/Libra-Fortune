package com.kuretru.web.libra.ledger.entity.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class LedgerEntryTagQuery {

    @Schema(description = "标签ID列表")
    private List<Long> tagIdIn;

}
