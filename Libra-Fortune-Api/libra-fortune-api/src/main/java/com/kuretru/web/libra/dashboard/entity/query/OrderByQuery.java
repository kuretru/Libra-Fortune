package com.kuretru.web.libra.dashboard.entity.query;

import com.kuretru.web.libra.dashboard.entity.enums.OrderByMode;
import com.kuretru.web.libra.dashboard.entity.enums.OrderByType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderByQuery {

    @NotNull
    @Schema(description = "排序类型")
    private OrderByType type;

    @NotBlank
    @Schema(description = "排序字段")
    private String name;

    @NotNull
    @Schema(description = "排序方向")
    private OrderByMode mode;

}
