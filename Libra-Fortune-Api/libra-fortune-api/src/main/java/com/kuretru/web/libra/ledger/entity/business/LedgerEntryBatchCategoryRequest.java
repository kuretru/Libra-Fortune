package com.kuretru.web.libra.ledger.entity.business;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class LedgerEntryBatchCategoryRequest {

    @NotEmpty
    @Size(max = 1000)
    @Schema(description = "条目ID列表，最多1000个")
    private List<@NotNull @Positive Long> entryIds;

    @NotNull
    @Positive
    @Schema(description = "一级分类ID")
    private Long categoryIdL1;

    @NotNull
    @Positive
    @Schema(description = "二级分类ID")
    private Long categoryIdL2;

}
