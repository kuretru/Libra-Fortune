package com.kuretru.web.libra.dashboard.service.impl;

import com.kuretru.web.libra.dashboard.entity.enums.ledger.LedgerDimensions;
import com.kuretru.web.libra.dashboard.entity.enums.ledger.LedgerMetrics;
import com.kuretru.web.libra.dashboard.entity.enums.ledger.LedgerTimeGroupBy;
import com.kuretru.web.libra.dashboard.entity.interfaces.Join;
import com.kuretru.web.libra.dashboard.entity.query.DashboardQuery;

import java.util.ArrayList;
import java.util.HashSet;

public class DashboardLedgerSqlProvider {

    public String buildQuery(DashboardQuery<LedgerTimeGroupBy, LedgerMetrics, LedgerDimensions> query) {
        var columns = new ArrayList<String>();
        var groupBys = new ArrayList<String>();
        var joins = new HashSet<Join>();

        var sql = new StringBuilder();
        sql.append("SELECT ");
        columns.add(query.getTime().getGroupBy().getSelect());
        groupBys.add(query.getTime().getGroupBy().getGroupBy());
        for (var groupBy : query.getDimensions()) {
            columns.add(groupBy.getSelect());
            groupBys.add(groupBy.getGroupBy());
            if (groupBy.getJoin() != null) {
                joins.add(groupBy.getJoin());
            }
        }
        for (var metric : query.getMetrics()) {
            columns.add(metric.getSelect());
            if (metric.getJoin() != null) {
                joins.add(metric.getJoin());
            }
        }
        sql.append(String.join(",", columns));

        sql.append(" FROM ledger_v2_entry entry");
        for (var join : joins) {
            sql.append(" ").append(join.getSql());
        }

        sql.append(" WHERE entry.`date` >= #{time.dateBegin}");
        sql.append(" AND entry.`date` <= #{time.dateEnd}");

        sql.append(" GROUP BY ");
        sql.append(String.join(",", groupBys));

        if (!query.getOrderBy().isEmpty()) {
            sql.append(" ORDER BY ");
            for (var orderBy : query.getOrderBy()) {
                sql.append(orderBy.getName()).append(" ");
                switch (orderBy.getMode()) {
                    case ASC:
                        sql.append("ASC").append(" ");
                        break;
                    case DESC:
                        sql.append("DESC").append(" ");
                        break;
                }
            }
        }

        if (query.getLimit() != null && query.getLimit() > 0) {
            sql.append(" LIMIT ").append(query.getLimit());
        }

        return sql.toString();
    }

}
