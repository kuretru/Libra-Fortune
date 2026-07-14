package com.kuretru.web.libra.dashboard.entity.query;

import com.kuretru.web.libra.dashboard.entity.enums.ledger.LedgerDimensions;
import com.kuretru.web.libra.dashboard.entity.enums.ledger.LedgerMetrics;
import com.kuretru.web.libra.dashboard.entity.enums.ledger.LedgerTimeGroupBy;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DashboardQuery extends BaseDashboardQuery<LedgerTimeGroupBy, LedgerMetrics, LedgerDimensions> {

    private String tableName = "ledger_v2_entry entry";

    private String timeColumnName = "entry.`date`";

}
