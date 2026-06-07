package com.kuretru.web.libra.tools.entity.transfer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CalculatorDTO {

    @NotNull
    @Schema(description = "第一个运算数")
    private BigDecimal x;

    @NotNull
    @Schema(description = "第二个运算数")
    private BigDecimal y;

    @Schema(description = "精度")
    private Integer accuracy;

}
