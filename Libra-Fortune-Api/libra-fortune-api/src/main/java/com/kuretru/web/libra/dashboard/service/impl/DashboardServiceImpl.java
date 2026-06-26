package com.kuretru.web.libra.dashboard.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.web.context.CurrentUserContext;
import com.kuretru.web.libra.account.entity.query.AccountBalanceQuery;
import com.kuretru.web.libra.account.service.AccountBalanceService;
import com.kuretru.web.libra.dashboard.entity.business.DashboardAccountBalanceBO;
import com.kuretru.web.libra.dashboard.entity.business.DashboardLedgerBO;
import com.kuretru.web.libra.dashboard.entity.query.DashboardLedgerQuery;
import com.kuretru.web.libra.dashboard.mapper.DashboardMapper;
import com.kuretru.web.libra.dashboard.service.DashboardService;
import com.kuretru.web.libra.ledger.entity.enums.LedgerGroupBy;
import com.kuretru.web.libra.ledger.entity.enums.LedgerSumMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final DashboardMapper mapper;
    private final AccountBalanceService accountBalanceService;

    @Autowired
    public DashboardServiceImpl(DashboardMapper mapper, AccountBalanceService accountBalanceService) {
        this.mapper = mapper;
        this.accountBalanceService = accountBalanceService;
    }

    @Override
    public List<DashboardLedgerBO> sum(DashboardLedgerQuery query) {
        var sumMode = query.getSumMode();
        String sum = sumMode.getSelect();

        var selectColumns = new ArrayList<String>();
        var groupByColumns = new ArrayList<String>();
        for (var groupBy : query.getGroupBy()) {
            selectColumns.add(groupBy.getSelect());
            groupByColumns.add(groupBy.getGroupBy());
        }

        var queryWrapper = new QueryWrapper<DashboardLedgerBO>();
        queryWrapper.ge("entry.`date`", query.getDateBegin());
        queryWrapper.le("entry.`date`", query.getDateEnd());
        var filter = query.getFilter();
        if (filter != null) {
            queryWrapper.in(hasItems(filter.getLedgerId()), "entry.ledger_id", filter.getLedgerId());
            queryWrapper.in(hasItems(filter.getCategoryIdL1()), "entry.category_id_l1", filter.getCategoryIdL1());
            queryWrapper.in(hasItems(filter.getCategoryIdL2()), "entry.category_id_l2", filter.getCategoryIdL2());
            queryWrapper.in(hasItems(filter.getType()), "entry.`type`", filter.getType());
            queryWrapper.in(hasItems(filter.getUsername()), "detail.username", filter.getUsername());
            queryWrapper.in(hasItems(filter.getTagId()), "tag.tag_id", filter.getTagId());
        }

        var joinDetail = sumMode == LedgerSumMode.FUNDED
                || query.getGroupBy().contains(LedgerGroupBy.USERNAME)
                || (filter != null && hasItems(filter.getUsername()));
        var joinTag = query.getGroupBy().contains(LedgerGroupBy.TAG_ID)
                || (filter != null && hasItems(filter.getTagId()));

        return mapper.sum(queryWrapper, sum, selectColumns, groupByColumns, joinDetail, joinTag, CurrentUserContext.getUsername());
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

    private boolean hasItems(List<?> values) {
        return values != null && !values.isEmpty();
    }

}
