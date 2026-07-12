package com.kuretru.web.libra.dashboard.entity.query;

import com.kuretru.web.libra.dashboard.entity.interfaces.Dimension;
import com.kuretru.web.libra.dashboard.entity.interfaces.Filter;
import com.kuretru.web.libra.dashboard.entity.interfaces.Metric;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class DashboardQuery<T extends Dimension, M extends Metric & Filter<Long>, D extends Dimension & Filter<String>> {

    @Valid
    @NotNull
    @Schema(description = "时间范围和分组")
    private TimeQuery<T> time;

    @NotEmpty
    @Schema(description = "指标列表")
    private List<M> metrics;

    @Schema(description = "维度列表")
    private List<D> dimensions;

    @Schema(description = "指标过滤条件")
    private List<FilterQuery<M, Long>> metricsFilter;

    @Schema(description = "维度过滤条件")
    private List<FilterQuery<D, String>> dimensionsFilter;

    @Valid
    @Schema(description = "排序条件")
    private List<OrderByQuery> orderBy;

    @Schema(description = "最大返回行数")
    private Integer limit;

}
