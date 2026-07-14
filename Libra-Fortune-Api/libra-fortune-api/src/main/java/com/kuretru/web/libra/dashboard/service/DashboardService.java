package com.kuretru.web.libra.dashboard.service;

import com.kuretru.web.libra.dashboard.entity.business.DashboardAccountBalanceBO;
import com.kuretru.web.libra.dashboard.entity.business.DashboardLedgerBO;
import com.kuretru.web.libra.dashboard.entity.query.DashboardLedgerQuery;
import com.kuretru.web.libra.dashboard.entity.query.DashboardQuery;

import java.util.List;

public interface DashboardService {

    List<DashboardLedgerBO> ledger(DashboardQuery query);

    List<DashboardLedgerBO> sum(DashboardLedgerQuery query);

    DashboardAccountBalanceBO latestAccountBalances();

}
