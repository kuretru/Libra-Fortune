package com.kuretru.web.libra.dashboard.service.impl;

import com.kuretru.microservices.dashboard.service.DashboardQueryValidator;
import com.kuretru.web.libra.account.entity.query.AccountBalanceQuery;
import com.kuretru.web.libra.account.service.AccountBalanceService;
import com.kuretru.web.libra.dashboard.entity.business.DashboardAccountBalanceBO;
import com.kuretru.web.libra.dashboard.entity.business.DashboardLedgerBO;
import com.kuretru.web.libra.dashboard.entity.query.DashboardLedgerQuery;
import com.kuretru.web.libra.dashboard.mapper.DashboardMapper;
import com.kuretru.web.libra.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final DashboardMapper mapper;
    private final AccountBalanceService accountBalanceService;
    private final DashboardQueryValidator dashboardQueryValidator;

    @Autowired
    public DashboardServiceImpl(DashboardMapper mapper, AccountBalanceService accountBalanceService) {
        this.mapper = mapper;
        this.accountBalanceService = accountBalanceService;
        this.dashboardQueryValidator = new DashboardQueryValidator();
    }

    @Override
    public List<DashboardLedgerBO> ledger(DashboardLedgerQuery query) {
        dashboardQueryValidator.validate(query);
        return mapper.query(query);
    }

    @Override
    public DashboardAccountBalanceBO latestAccountBalances() {
        var result = accountBalanceService.list(new AccountBalanceQuery());
        var dashboardResult = new DashboardAccountBalanceBO();
        if (!result.getBalances().isEmpty()) {
            var latestBalance = result.getBalances().getFirst();
            dashboardResult.setDate(latestBalance.getDate());
            dashboardResult.setTotalBalance(latestBalance.getTotalBalance());
        }
        return dashboardResult;
    }

}
