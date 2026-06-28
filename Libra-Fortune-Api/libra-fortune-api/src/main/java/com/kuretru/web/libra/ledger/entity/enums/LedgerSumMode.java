package com.kuretru.web.libra.ledger.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kuretru.microservices.common.entity.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum LedgerSumMode implements BaseEnum<LedgerSumMode> {

    ORIGINAL("original", "原始金额", "SUM(entry.original_amount) AS sum"),
    SETTLEMENT("settlement", "结算金额", "SUM(entry.settlement_amount) AS sum"),
    FUNDED("funded", "分担金额", "SUM(detail.amount) AS sum");

    @EnumValue
    @JsonValue
    private final String value;
    private final String label;
    private final String select;

    LedgerSumMode(String value, String label, String select) {
        this.value = value;
        this.label = label;
        this.select = select;
    }

}
