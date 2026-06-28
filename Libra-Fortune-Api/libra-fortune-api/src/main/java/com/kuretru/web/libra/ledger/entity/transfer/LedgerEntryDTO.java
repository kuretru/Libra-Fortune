package com.kuretru.web.libra.ledger.entity.transfer;

import com.kuretru.microservices.web.v2.entity.transfer.BaseCreateUpdateDTO;
import com.kuretru.web.libra.ledger.entity.enums.EntryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LedgerEntryDTO extends BaseCreateUpdateDTO {

    @Schema(description = "账本ID")
    private Long ledgerId;

    @NotNull
    @Schema(description = "一级分类ID")
    private Long categoryIdL1;

    @NotNull
    @Schema(description = "二级分类ID")
    private Long categoryIdL2;

    @NotNull
    @Schema(description = "枚举值，条目类型")
    private EntryType type;

    @NotNull
    @Schema(description = "交易日期")
    private LocalDate date;

    @NotEmpty
    @Size(min = 1, max = 32)
    @Schema(description = "条目名称")
    private String name;

    @NotNull
    @Schema(description = "原始消费金额")
    private BigDecimal originalAmount;

    @NotEmpty
    @Size(min = 3, max = 3)
    @Schema(description = "原始消费货币")
    private String originalCurrency;

    @NotNull
    @Schema(description = "结算金额")
    private BigDecimal settlementAmount;

    @NotEmpty
    @Size(min = 3, max = 3)
    @Schema(description = "结算货币")
    private String settlementCurrency;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "关联标签列表")
    private List<LedgerEntryTagDTO> tags;

    @Schema(description = "条目明细")
    private List<LedgerEntryDetailDTO> details;

}
