package com.kuretru.web.libra.tools.entity.transfer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculatorResult {

    @Schema(description = "计算结果")
    private BigDecimal result;

}
