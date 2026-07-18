package com.kuretru.web.libra.dashboard.service.impl;

import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.web.libra.account.entity.query.AccountBalanceQuery;
import com.kuretru.web.libra.account.service.AccountBalanceService;
import com.kuretru.web.libra.dashboard.entity.business.DashboardAccountBalanceBO;
import com.kuretru.web.libra.dashboard.entity.business.DashboardLedgerBO;
import com.kuretru.web.libra.dashboard.entity.interfaces.Field;
import com.kuretru.web.libra.dashboard.entity.query.DashboardLedgerQuery;
import com.kuretru.web.libra.dashboard.entity.query.FilterQuery;
import com.kuretru.web.libra.dashboard.mapper.DashboardMapper;
import com.kuretru.web.libra.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public List<DashboardLedgerBO> ledger(DashboardLedgerQuery query) {
        verifyFilter(query.getMetricsFilter(), new HashSet<>(query.getMetrics()), true, "指标过滤字段");
        verifyFilter(query.getDimensionsFilter(), null, false, "维度过滤字段");
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

    private boolean hasItems(List<?> values) {
        return values != null && !values.isEmpty();
    }

    private <T extends Field> void verifyFilter(FilterQuery<T> filter, Set<T> selectedFields,
                                                boolean requireSelectedField, String fieldLabel) {
        if (filter == null) {
            return;
        }
        if (hasItems(filter.getChildren())) {
            verifyFilterGroup(filter, fieldLabel);
            for (var child : filter.getChildren()) {
                verifyFilter(child, selectedFields, requireSelectedField, fieldLabel);
            }
            return;
        }
        verifyFilterLeaf(filter, selectedFields, requireSelectedField, fieldLabel);
    }

    private <T extends Field> void verifyFilterGroup(FilterQuery<T> filter, String fieldLabel) {
        if (filter.getLogic() == null) {
            throw requestParameterError(fieldLabel + "分组缺少logic");
        }
        if (filter.getName() != null || filter.getOperator() != null || hasItems(filter.getValues())) {
            throw requestParameterError(fieldLabel + "分组不能同时指定name/operator/values");
        }
    }

    private <T extends Field> void verifyFilterLeaf(FilterQuery<T> filter, Set<T> selectedFields,
                                                    boolean requireSelectedField, String fieldLabel) {
        if (filter.getLogic() != null) {
            throw requestParameterError(fieldLabel + "叶子节点不能指定logic");
        }
        if (filter.getName() == null) {
            throw requestParameterError(fieldLabel + "缺少name");
        }
        if (filter.getOperator() == null) {
            throw requestParameterError(fieldLabel + "缺少operator");
        }
        if (requireSelectedField && !selectedFields.contains(filter.getName())) {
            throw requestParameterError(fieldLabel + "不在本次查询字段中: " + filter.getName().getValue());
        }
        if (!hasItems(filter.getValues())) {
            throw requestParameterError(fieldLabel + "缺少values");
        }
        switch (filter.getOperator()) {
            case IN:
            case NOT_IN:
                break;
            default:
                if (filter.getValues().size() != 1) {
                    throw requestParameterError(fieldLabel + "当前operator只能指定一个value");
                }
        }
    }

    private ServiceException requestParameterError(String message) {
        return ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, message);
    }

}
