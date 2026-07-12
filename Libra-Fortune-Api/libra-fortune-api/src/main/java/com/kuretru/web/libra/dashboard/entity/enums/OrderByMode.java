package com.kuretru.web.libra.dashboard.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kuretru.microservices.common.entity.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum OrderByMode implements BaseEnum<OrderByMode> {

    ASC("asc", "升序"),
    DESC("desc", "降序");

    @EnumValue
    @JsonValue
    private final String value;
    private final String label;

    OrderByMode(String value, String label) {
        this.value = value;
        this.label = label;
    }

}
