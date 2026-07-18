package com.kuretru.web.libra.dashboard.mapper;

import com.kuretru.web.libra.dashboard.entity.enums.FilterLogic;
import com.kuretru.web.libra.dashboard.entity.enums.FilterOperator;
import com.kuretru.web.libra.dashboard.entity.enums.OrderByType;
import com.kuretru.web.libra.dashboard.entity.interfaces.Field;
import com.kuretru.web.libra.dashboard.entity.interfaces.Join;
import com.kuretru.web.libra.dashboard.entity.query.DashboardQuery;
import com.kuretru.web.libra.dashboard.entity.query.FilterQuery;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DashboardLedgerSqlProvider {

    public String buildQuery(DashboardQuery query) {
        var columns = new ArrayList<String>();
        var groupBys = new ArrayList<String>();
        var joins = new HashSet<Join>();

        var sql = new StringBuilder();
        sql.append("SELECT ");
        columns.add(query.getTime().getGroupBy().getColumn() + " AS " + query.getTime().getGroupBy().getValue());
        groupBys.add(query.getTime().getGroupBy().getColumn());
        for (var groupBy : emptyIfNull(query.getDimensions())) {
            columns.add(groupBy.getColumn() + " AS " + groupBy.getValue());
            groupBys.add(groupBy.getColumn());
            if (groupBy.getJoin() != null) {
                joins.add(groupBy.getJoin());
            }
        }
        for (var metric : query.getMetrics()) {
            columns.add(metric.getColumn() + " AS " + metric.getValue());
            if (metric.getJoin() != null) {
                joins.add(metric.getJoin());
            }
        }
        collectFilterJoins(query.getDimensionsFilter(), joins);
        collectFilterJoins(query.getMetricsFilter(), joins);
        sql.append(String.join(", ", columns));

        sql.append(" FROM ").append(query.getTableName());
        for (var join : joins) {
            sql.append(" ").append(join.getSql());
        }

        sql.append(" WHERE ").append(query.getTimeColumnName()).append(" >= #{time.dateBegin}");
        sql.append(" AND ").append(query.getTimeColumnName()).append(" <= #{time.dateEnd}");
        var dimensionsFilter = buildFilter(query.getDimensionsFilter(), "dimensionsFilter");
        if (!dimensionsFilter.isBlank()) {
            sql.append(" AND ").append(dimensionsFilter);
        }

        sql.append(" GROUP BY ");
        sql.append(String.join(", ", groupBys));

        var metricsFilter = buildFilter(query.getMetricsFilter(), "metricsFilter");
        if (!metricsFilter.isBlank()) {
            sql.append(" HAVING ").append(metricsFilter);
        }

        var orderBySql = buildOrderBy(query);
        if (!orderBySql.isBlank()) {
            sql.append(" ORDER BY ").append(orderBySql);
        }

        if (query.getLimit() != null && query.getLimit() > 0) {
            sql.append(" LIMIT ").append(query.getLimit());
        }

        return sql.toString();
    }

    private <T> List<T> emptyIfNull(List<T> values) {
        return values == null ? List.of() : values;
    }

    private <T extends Field> void collectFilterJoins(FilterQuery<T> filter, Set<Join> joins) {
        if (filter == null) {
            return;
        }
        if (!CollectionUtils.isEmpty(filter.getChildren())) {
            for (var child : filter.getChildren()) {
                collectFilterJoins(child, joins);
            }
            return;
        }
        if (filter.getName() != null && filter.getName().getJoin() != null) {
            joins.add(filter.getName().getJoin());
        }
    }

    private <T extends Field> String buildFilter(FilterQuery<T> filter, String parameterPath) {
        if (filter == null) {
            return "";
        }
        if (!CollectionUtils.isEmpty(filter.getChildren())) {
            validateGroupNode(filter);
            var children = new ArrayList<String>();
            for (var i = 0; i < filter.getChildren().size(); i++) {
                var childSql = buildFilter(filter.getChildren().get(i), parameterPath + ".children[" + i + "]");
                if (!childSql.isBlank()) {
                    children.add(childSql);
                }
            }
            if (children.isEmpty()) {
                return "";
            }
            return "(" + String.join(joiner(filter.getLogic()), children) + ")";
        }
        validateLeafNode(filter);
        return buildLeafFilter(filter, parameterPath);
    }

    private <T extends Field> void validateGroupNode(FilterQuery<T> filter) {
        if (filter.getLogic() == null) {
            throw new IllegalArgumentException("filter group logic is required");
        }
        if (filter.getName() != null || filter.getOperator() != null || !CollectionUtils.isEmpty(filter.getValues())) {
            throw new IllegalArgumentException("filter group cannot contain name/operator/values");
        }
    }

    private <T extends Field> void validateLeafNode(FilterQuery<T> filter) {
        if (filter.getLogic() != null) {
            throw new IllegalArgumentException("filter leaf cannot contain logic");
        }
        if (filter.getName() == null) {
            throw new IllegalArgumentException("filter name is required");
        }
        if (filter.getOperator() == null) {
            throw new IllegalArgumentException("filter operator is required");
        }
        if (CollectionUtils.isEmpty(filter.getValues())) {
            throw new IllegalArgumentException("filter values is required");
        }
        switch (filter.getOperator()) {
            case IN:
            case NOT_IN:
                break;
            default:
                if (filter.getValues().size() != 1) {
                    throw new IllegalArgumentException("filter operator requires exactly one value");
                }
        }
    }

    private String joiner(FilterLogic logic) {
        return switch (logic) {
            case AND -> " AND ";
            case OR -> " OR ";
        };
    }

    private <T extends Field> String buildLeafFilter(FilterQuery<T> filter, String parameterPath) {
        var column = filter.getName().getColumn();
        var operator = filter.getOperator();
        return switch (operator) {
            case EQUAL -> column + " = " + valuePlaceholder(parameterPath, 0);
            case NOT_EQUAL -> column + " <> " + valuePlaceholder(parameterPath, 0);
            case IN -> column + " IN (" + valuePlaceholders(parameterPath, filter.getValues().size()) + ")";
            case NOT_IN -> column + " NOT IN (" + valuePlaceholders(parameterPath, filter.getValues().size()) + ")";
            case LIKE -> column + " LIKE CONCAT('%', " + valuePlaceholder(parameterPath, 0) + ", '%')";
            case NOT_LIKE -> column + " NOT LIKE CONCAT('%', " + valuePlaceholder(parameterPath, 0) + ", '%')";
            case GT -> column + " > " + valuePlaceholder(parameterPath, 0);
            case GTE -> column + " >= " + valuePlaceholder(parameterPath, 0);
            case LT -> column + " < " + valuePlaceholder(parameterPath, 0);
            case LTE -> column + " <= " + valuePlaceholder(parameterPath, 0);
        };
    }

    private String valuePlaceholders(String parameterPath, int size) {
        var placeholders = new ArrayList<String>();
        for (var i = 0; i < size; i++) {
            placeholders.add(valuePlaceholder(parameterPath, i));
        }
        return String.join(", ", placeholders);
    }

    private String valuePlaceholder(String parameterPath, int index) {
        return "#{" + parameterPath + ".values[" + index + "]}";
    }

    private String buildOrderBy(DashboardQuery query) {
        if (CollectionUtils.isEmpty(query.getOrderBy())) {
            return "";
        }
        var orderBySql = new ArrayList<String>();
        for (var orderBy : query.getOrderBy()) {
            var alias = resolveOrderByAlias(query, orderBy.getType(), orderBy.getName());
            orderBySql.add(alias + " " + orderBy.getMode().name());
        }
        return String.join(", ", orderBySql);
    }

    private String resolveOrderByAlias(DashboardQuery query, OrderByType type, String name) {
        return switch (type) {
            case TIME -> {
                var groupBy = query.getTime().getGroupBy();
                if (groupBy.getValue().equals(name)) {
                    yield groupBy.getValue();
                }
                throw new IllegalArgumentException("order by time field is not selected: " + name);
            }
            case METRIC -> resolveSelectedFieldAlias(query.getMetrics(), name, "metric");
            case DIMENSION -> resolveSelectedFieldAlias(emptyIfNull(query.getDimensions()), name, "dimension");
        };
    }

    private String resolveSelectedFieldAlias(List<? extends Field> fields, String name, String fieldType) {
        for (var field : fields) {
            if (field.getValue().equals(name)) {
                return field.getValue();
            }
        }
        throw new IllegalArgumentException("order by " + fieldType + " field is not selected: " + name);
    }

}
