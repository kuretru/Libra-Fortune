package com.kuretru.web.libra.dashboard.entity.query;

import com.kuretru.web.libra.dashboard.entity.interfaces.Dimension;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class TimeQuery<T extends Dimension> {

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "交易日期开始")
    private LocalDate dateBegin;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "交易日期结束")
    private LocalDate dateEnd;

    @NotNull
    @Schema(description = "时间分组")
    private T groupBy;

}
