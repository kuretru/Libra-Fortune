package com.kuretru.web.libra.dashboard.entity.enums.ledger;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kuretru.microservices.common.entity.enums.BaseEnum;
import com.kuretru.web.libra.dashboard.entity.interfaces.Field;
import com.kuretru.web.libra.dashboard.entity.interfaces.Join;
import lombok.Getter;

@Getter
public enum LedgerMetrics implements BaseEnum<LedgerMetrics>, Field {

    ORIGINAL_SUM("originalSum", "原始金额合计", "SUM(entry.original_amount)", null),
    SETTLEMENT_SUM("settlementSum", "结算金额合计", "SUM(entry.settlement_amount)", null),
    FUNDED_SUM("fundedSum", "分担金额合计", "SUM(detail.amount)", LedgerJoin.DETAIL);

    @EnumValue
    @JsonValue
    private final String value;
    private final String label;
    private final String column;
    private final Join join;

    LedgerMetrics(String value, String label, String column, Join join) {
        this.value = value;
        this.label = label;
        this.column = column;
        this.join = join;
    }
}
