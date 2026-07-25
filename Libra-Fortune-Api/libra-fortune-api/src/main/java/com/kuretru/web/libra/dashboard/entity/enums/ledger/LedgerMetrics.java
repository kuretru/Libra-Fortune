package com.kuretru.web.libra.dashboard.entity.enums.ledger;

import com.kuretru.microservices.dashboard.entity.interfaces.Field;
import com.kuretru.microservices.dashboard.entity.interfaces.Join;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kuretru.microservices.common.entity.enums.BaseEnum;
import lombok.Getter;

import java.util.List;

@Getter
public enum LedgerMetrics implements BaseEnum<LedgerMetrics>, Field {

    ORIGINAL_SUM("originalSum", "原始金额合计", "SUM(entry.original_amount)", List.of()),
    SETTLEMENT_SUM("settlementSum", "结算金额合计", "SUM(entry.settlement_amount)", List.of()),
    FUNDED_SUM("fundedSum", "分担金额合计", "SUM(detail.amount)", List.of(LedgerJoin.DETAIL));

    @EnumValue
    @JsonValue
    private final String value;
    private final String label;
    private final String column;
    private final List<Join> joins;

    LedgerMetrics(String value, String label, String column, List<Join> joins) {
        this.value = value;
        this.label = label;
        this.column = column;
        this.joins = joins;
    }
}
