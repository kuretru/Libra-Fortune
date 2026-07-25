package com.kuretru.web.libra.dashboard.mapper;

import com.kuretru.microservices.web.entity.enums.SortOrderEnum;
import com.kuretru.web.libra.dashboard.entity.enums.OrderByType;
import com.kuretru.web.libra.dashboard.entity.enums.ledger.LedgerDimensions;
import com.kuretru.web.libra.dashboard.entity.enums.ledger.LedgerMetrics;
import com.kuretru.web.libra.dashboard.entity.enums.ledger.LedgerTimeDimension;
import com.kuretru.web.libra.dashboard.entity.query.DashboardLedgerQuery;
import com.kuretru.web.libra.dashboard.entity.query.OrderByQuery;
import com.kuretru.web.libra.dashboard.entity.query.TimeQuery;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DashboardLedgerSqlProviderTest {

    private final DashboardLedgerSqlProvider sqlProvider = new DashboardLedgerSqlProvider();

    @Test
    void buildQueryAddsTagSetDependencyJoinsInOrder() {
        var query = buildQuery();
        var time = query.getTime();
        time.setDimension(LedgerTimeDimension.MONTHLY);
        query.setDimensions(List.of(LedgerDimensions.TAG_SET_ID));

        var sql = sqlProvider.buildQuery(query);

        var tagJoinIndex = sql.indexOf("LEFT JOIN ledger_v2_entry_tag tag");
        var tagItemJoinIndex = sql.indexOf("LEFT JOIN metadata_tag_set_item tag_item");
        assertTrue(tagJoinIndex >= 0);
        assertTrue(tagItemJoinIndex > tagJoinIndex);
    }

    @Test
    void buildQueryDoesNotGroupByTimeWhenTimeDimensionIsNull() {
        var query = buildQuery();

        var sql = sqlProvider.buildQuery(query);

        assertTrue(sql.startsWith("SELECT SUM(entry.original_amount) AS originalSum FROM"));
        assertFalse(sql.contains(" GROUP BY "));
    }

    @Test
    void buildQueryStillGroupsBySelectedDimensionsWhenTimeDimensionIsNull() {
        var query = buildQuery();
        query.setDimensions(List.of(LedgerDimensions.LEDGER_ID));

        var sql = sqlProvider.buildQuery(query);

        assertTrue(sql.startsWith("SELECT entry.ledger_id AS ledgerId, SUM(entry.original_amount) AS originalSum FROM"));
        assertTrue(sql.contains(" GROUP BY entry.ledger_id"));
    }

    @Test
    void buildQueryTranslatesSortOrderValuesToSqlKeywords() {
        var query = buildQuery();
        var ascend = new OrderByQuery();
        ascend.setType(OrderByType.METRIC);
        ascend.setName(LedgerMetrics.ORIGINAL_SUM.getValue());
        ascend.setMode(SortOrderEnum.ASCEND);
        var descend = new OrderByQuery();
        descend.setType(OrderByType.METRIC);
        descend.setName(LedgerMetrics.ORIGINAL_SUM.getValue());
        descend.setMode(SortOrderEnum.DESCEND);
        query.setOrderBy(List.of(ascend, descend));

        var sql = sqlProvider.buildQuery(query);

        assertTrue(sql.contains(" ORDER BY originalSum ASC, originalSum DESC"));
    }

    private DashboardLedgerQuery buildQuery() {
        var query = new DashboardLedgerQuery();
        var time = new TimeQuery<LedgerTimeDimension>();
        time.setDateBegin(LocalDate.of(2026, 1, 1));
        time.setDateEnd(LocalDate.of(2026, 7, 31));
        query.setTime(time);
        query.setMetrics(List.of(LedgerMetrics.ORIGINAL_SUM));
        return query;
    }

}
