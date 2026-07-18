package com.kuretru.web.libra.dashboard.mapper;

import com.kuretru.web.libra.dashboard.entity.enums.ledger.LedgerDimensions;
import com.kuretru.web.libra.dashboard.entity.enums.ledger.LedgerMetrics;
import com.kuretru.web.libra.dashboard.entity.enums.ledger.LedgerTimeDimension;
import com.kuretru.web.libra.dashboard.entity.query.DashboardLedgerQuery;
import com.kuretru.web.libra.dashboard.entity.query.TimeQuery;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DashboardLedgerSqlProviderTest {

    private final DashboardLedgerSqlProvider sqlProvider = new DashboardLedgerSqlProvider();

    @Test
    void buildQueryAddsTagSetDependencyJoinsInOrder() {
        var query = new DashboardLedgerQuery();
        var time = new TimeQuery<LedgerTimeDimension>();
        time.setDateBegin(LocalDate.of(2026, 1, 1));
        time.setDateEnd(LocalDate.of(2026, 7, 31));
        time.setDimension(LedgerTimeDimension.MONTHLY);
        query.setTime(time);
        query.setMetrics(List.of(LedgerMetrics.ORIGINAL_SUM));
        query.setDimensions(List.of(LedgerDimensions.TAG_SET_ID));

        var sql = sqlProvider.buildQuery(query);

        var tagJoinIndex = sql.indexOf("LEFT JOIN ledger_v2_entry_tag tag");
        var tagItemJoinIndex = sql.indexOf("LEFT JOIN metadata_tag_set_item tag_item");
        assertTrue(tagJoinIndex >= 0);
        assertTrue(tagItemJoinIndex > tagJoinIndex);
    }

}
