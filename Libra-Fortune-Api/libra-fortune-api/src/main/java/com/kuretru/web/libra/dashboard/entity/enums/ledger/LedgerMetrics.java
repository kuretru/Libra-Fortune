package com.kuretru.web.libra.dashboard.entity.enums.ledger;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kuretru.microservices.common.entity.enums.BaseEnum;
import com.kuretru.web.libra.dashboard.entity.interfaces.Filter;
import com.kuretru.web.libra.dashboard.entity.interfaces.Join;
import com.kuretru.web.libra.dashboard.entity.interfaces.Metric;
import lombok.Getter;

@Getter
public enum LedgerMetrics implements BaseEnum<LedgerMetrics>, Metric, Filter<Long> {

    ORIGINAL_SUM("original_sum", "原始金额合计", "SUM(entry.original_amount) AS original_sum", null),
    SETTLEMENT_SUM("settlement_sum", "结算金额合计", "SUM(entry.settlement_amount) AS settlement_sum)", null),
    FUNDED_SUM("funded_sum", "分担金额合计", "SUM(detail.amount) AS funded_sum", LedgerJoin.DETAIL);

    @EnumValue
    @JsonValue
    private final String value;
    private final String label;
    private final String select;
    private final Join join;

    LedgerMetrics(String value, String label, String select, Join join) {
        this.value = value;
        this.label = label;
        this.select = select;
        this.join = join;
    }
}
