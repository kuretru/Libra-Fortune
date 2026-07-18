package com.kuretru.web.libra.dashboard.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kuretru.microservices.common.entity.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum FilterOperator implements BaseEnum<FilterOperator> {
    EQUAL("equal", "相等"),
    NOT_EQUAL("not_equal", "不相等"),
    IN("in", "IN"),
    NOT_IN("not_in", "NOT-IN"),
    LIKE("like", "LIKE"),
    NOT_LIKE("not_like", "NOT-LIKE"),
    GT("gt", "大于"),
    GTE("gte", "大于等于"),
    LT("lt", "小于"),
    LTE("lte", "小于等于");

    @EnumValue
    @JsonValue
    private final String value;
    private final String label;

    FilterOperator(String value, String label) {
        this.value = value;
        this.label = label;
    }

}
