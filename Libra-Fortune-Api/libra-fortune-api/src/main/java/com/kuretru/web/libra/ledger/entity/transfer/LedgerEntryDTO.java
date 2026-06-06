package com.kuretru.web.libra.ledger.entity.transfer;

import com.kuretru.microservices.web.v2.entity.transfer.BaseCreateUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LedgerEntryDTO extends BaseCreateUpdateDTO {

    @Schema(description = "账本ID")
    private Long ledgerId;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "交易日期")
    private LocalDate date;

    @Schema(description = "条目名称")
    private String name;

    @Schema(description = "原始消费金额")
    private String originalAmount;

    @Schema(description = "原始消费货币")
    private String originalCurrency;

    @Schema(description = "结算金额")
    private String settlementAmount;

    @Schema(description = "结算货币")
    private String settlementCurrency;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "关联标签列表")
    private List<Long> tags;

    @Schema(description = "条目明细")
    private List<LedgerEntryDetailDTO> details;

}
