package com.kuretru.web.libra.ledger.entity.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
public class LedgerEntryQuery {

    @Schema(description = "账本ID")
    private Long ledgerId;

    @Schema(description = "分类路径；一级分类传一级ID，二级分类传一级ID,二级ID")
    private String category;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "交易日期开始")
    private LocalDate dateBegin;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "交易日期结束")
    private LocalDate dateEnd;

    @Schema(description = "条目名称，模糊查询")
    private String nameLike;

    @Schema(description = "原始消费货币")
    private String originalCurrency;

    @Schema(description = "结算货币")
    private String settlementCurrency;

    @Schema(description = "标签ID")
    private List<Long> tagIdIn;

}
