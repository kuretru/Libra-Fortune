package com.kuretru.web.libra.ledger.entity.query;

import com.kuretru.web.libra.ledger.entity.enums.LedgerGroupBy;
import com.kuretru.web.libra.ledger.entity.enums.LedgerSumMode;
import com.kuretru.web.libra.ledger.entity.enums.EntryType;
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

        @Schema(description = "一级分类ID")
        private List<Long> categoryIdL1;

        @Schema(description = "二级分类ID")
        private List<Long> categoryIdL2;

        @Schema(description = "条目类型")
        private List<EntryType> type;

        @Schema(description = "用户名")
        private List<String> username;

        @Schema(description = "标签ID")
        private List<Long> tagId;

    }

}
