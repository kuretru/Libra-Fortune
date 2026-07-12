package com.kuretru.web.libra.dashboard.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kuretru.microservices.common.entity.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum OrderByType implements BaseEnum<OrderByType> {

    TIME("time", "时间"),
    METRIC("metric", "指标"),
    DIMENSION("dimension", "维度");

    @EnumValue
    @JsonValue
    private final String value;
    private final String label;

    OrderByType(String value, String label) {
        this.value = value;
        this.label = label;
    }

}
