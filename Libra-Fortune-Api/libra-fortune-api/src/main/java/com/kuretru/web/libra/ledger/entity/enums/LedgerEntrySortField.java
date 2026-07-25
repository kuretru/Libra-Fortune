package com.kuretru.web.libra.ledger.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kuretru.microservices.common.entity.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum LedgerEntrySortField implements BaseEnum<LedgerEntrySortField> {

    ORIGINAL_AMOUNT("originalAmount", "原始金额", "original_amount"),
    SETTLEMENT_AMOUNT("settlementAmount", "结算金额", "settlement_amount");

    @EnumValue
    @JsonValue
    private final String value;
    private final String label;
    private final String column;

    LedgerEntrySortField(String value, String label, String column) {
        this.value = value;
        this.label = label;
        this.column = column;
    }

}
