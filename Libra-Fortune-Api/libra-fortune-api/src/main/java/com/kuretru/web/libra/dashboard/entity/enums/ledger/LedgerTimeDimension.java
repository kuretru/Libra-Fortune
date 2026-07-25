package com.kuretru.web.libra.dashboard.entity.enums.ledger;

import com.kuretru.microservices.dashboard.entity.interfaces.Field;
import com.kuretru.microservices.dashboard.entity.interfaces.Join;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kuretru.microservices.common.entity.enums.BaseEnum;
import lombok.Getter;

import java.util.List;

@Getter
public enum LedgerTimeDimension implements BaseEnum<LedgerTimeDimension>, Field {

    DAILY("day", "按日", "entry.`date`", List.of()),
    WEEKLY("week", "按周", "DATE_FORMAT(DATE_SUB(entry.`date`, INTERVAL (DAYOFWEEK(entry.`date`) - 1) DAY), '%Y-%m-%d')", List.of()),
    MONTHLY("month", "按月", "DATE_FORMAT(entry.`date`, '%Y-%m')", List.of()),
    YEARLY("year", "按年", "YEAR(entry.`date`)", List.of());

    @EnumValue
    @JsonValue
    private final String value;
    private final String label;
    private final String column;
    private final List<Join> joins;

    LedgerTimeDimension(String value, String label, String column, List<Join> joins) {
        this.value = value;
        this.label = label;
        this.column = column;
        this.joins = joins;
    }
}
