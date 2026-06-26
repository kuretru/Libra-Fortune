package com.kuretru.web.libra.ledger.entity.query;

import com.kuretru.web.libra.ledger.entity.enums.LedgerGroupBy;
import com.kuretru.web.libra.ledger.entity.enums.LedgerSumMode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
public class DashboardQuery {

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "交易日期开始")
    private LocalDate dateBegin;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "交易日期结束")
    private LocalDate dateEnd;

    @NotNull
    @Schema(description = "求和字段")
    private LedgerSumMode sumMode;

    @NotEmpty
    @Schema(description = "分组依据")
    private List<LedgerGroupBy> groupBy;

    @Schema(description = "过滤条件")
    private Filter filter;

    @Data
    public static class Filter {

        @Schema(description = "账本ID")
        private List<Long> ledgerId;

    }

}
