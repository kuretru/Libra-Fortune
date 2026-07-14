package com.kuretru.web.libra.dashboard.entity.enums.ledger;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kuretru.microservices.common.entity.enums.BaseEnum;
import com.kuretru.web.libra.dashboard.entity.interfaces.Field;
import com.kuretru.web.libra.dashboard.entity.interfaces.Join;
import lombok.Getter;

@Getter
public enum LedgerTimeGroupBy implements BaseEnum<LedgerTimeGroupBy>, Field {

    DAILY("day", "按日", "entry.`date`", null),
    WEEKLY("week", "按周", "", null),
    MONTHLY("month", "按月", "DATE_FORMAT(entry.`date`, '%Y-%m')", null),
    YEARLY("year", "按年", "YEAR(entry.`date`)", null);

    @EnumValue
    @JsonValue
    private final String value;
    private final String label;
    private final String column;
    private final Join join;

    LedgerTimeGroupBy(String value, String label, String column, Join join) {
        this.value = value;
        this.label = label;
        this.column = column;
        this.join = join;
    }
}
