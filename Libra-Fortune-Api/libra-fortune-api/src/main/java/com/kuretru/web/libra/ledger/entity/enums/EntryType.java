package com.kuretru.web.libra.ledger.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kuretru.microservices.common.entity.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum EntryType implements BaseEnum<EntryType> {

    EXPENSE("expense", "支出"),
    INCOME("income", "收入"),
    TRANSFER("transfer", "转账");

    @EnumValue
    @JsonValue
    private final String value;
    private final String label;

    EntryType(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
