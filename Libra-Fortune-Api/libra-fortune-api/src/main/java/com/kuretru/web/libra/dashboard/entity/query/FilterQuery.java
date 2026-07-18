package com.kuretru.web.libra.dashboard.entity.query;

import com.kuretru.web.libra.dashboard.entity.enums.FilterLogic;
import com.kuretru.web.libra.dashboard.entity.enums.FilterOperator;
import com.kuretru.web.libra.dashboard.entity.interfaces.Field;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class FilterQuery<T extends Field> {

    @Schema(description = "组内运算符")
    private FilterLogic logic;

    @Schema(description = "子组")
    private List<FilterQuery<T>> children;

    @Schema(description = "字段")
    private T name;

    @Schema(description = "算子")
    private FilterOperator operator;

    @Schema(description = "过滤值")
    private List<String> values;

}
