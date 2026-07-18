package com.kuretru.web.libra.dashboard.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kuretru.microservices.common.entity.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum FilterLogic implements BaseEnum<FilterLogic> {

    AND("and", "AND"),
    OR("or", "OR");

    @EnumValue
    @JsonValue
    private final String value;
    private final String label;

    FilterLogic(String value, String label) {
        this.value = value;
        this.label = label;
    }

}
