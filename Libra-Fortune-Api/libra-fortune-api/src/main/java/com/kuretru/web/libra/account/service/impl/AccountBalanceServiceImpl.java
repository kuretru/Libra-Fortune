package com.kuretru.web.libra.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.context.CurrentUserContext;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.web.libra.account.entity.business.AccountBalanceBO;
import com.kuretru.web.libra.account.entity.business.AccountBalanceDateBO;
import com.kuretru.web.libra.account.entity.business.AccountBalanceRequest;
import com.kuretru.web.libra.account.entity.business.AccountBalanceResultBO;
import com.kuretru.web.libra.account.entity.data.AccountBalanceDO;
import com.kuretru.web.libra.account.entity.mapper.AccountBalanceEntityMapper;
import com.kuretru.web.libra.account.entity.query.AccountBalanceQuery;
import com.kuretru.web.libra.account.entity.query.AccountQuery;
import com.kuretru.web.libra.account.entity.transfer.AccountDTO;
import com.kuretru.web.libra.account.mapper.AccountBalanceMapper;
import com.kuretru.web.libra.account.service.AccountBalanceService;
import com.kuretru.web.libra.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AccountBalanceServiceImpl implements AccountBalanceService {

    private final AccountBalanceMapper mapper;
    private final AccountService accountService;

    @Autowired
    public AccountBalanceServiceImpl(AccountBalanceMapper mapper, AccountBalanceEntityMapper entityMapper,
                                     AccountService accountService) {
        this.mapper = mapper;
        this.accountService = accountService;
    }

    @Override
    public AccountBalanceResultBO list(AccountBalanceQuery query) {
        var accountList = listAccount();
        var accountIdList = accountList.stream().map(AccountDTO::getId).toList();
        var records = listBalance(accountIdList, query);

        var result = new AccountBalanceResultBO();
        result.setAccounts(accountList);
        result.setBalances(new ArrayList<>());
        var dateMap = new HashMap<LocalDate, AccountBalanceDateBO>();
        for (var record : records) {
            var dateBo = dateMap.get(record.getDate());
            if (dateBo == null) {
                dateBo = new AccountBalanceDateBO();
                dateBo.setDate(record.getDate());
                dateBo.setTotalBalance(new BigDecimal("0.00"));
                dateBo.setItems(new ArrayList<>());
                dateMap.put(record.getDate(), dateBo);
                result.getBalances().add(dateBo);
            }
            dateBo.setTotalBalance(dateBo.getTotalBalance().add(record.getBalance()));
            var bo = new AccountBalanceBO();
            bo.setAccountId(record.getAccountId());
            bo.setBalance(record.getBalance());
            dateBo.getItems().add(bo);
        }

        return result;
    }

    private List<AccountDTO> listAccount() {
        var accountQuery = new AccountQuery();
        accountQuery.setOwner(CurrentUserContext.getUsername());
        accountQuery.setCanHoldFunds(true);
        return accountService.list(accountQuery);
    }

    private List<AccountBalanceDO> listBalance(List<Long> accountIdList, AccountBalanceQuery query) {
        var queryWrapper = new QueryWrapper<AccountBalanceDO>();
        queryWrapper.in("account_id", accountIdList);
        if (query.getDateBegin() != null) {
            queryWrapper.ge("date", query.getDateBegin());
        }
        if (query.getDateEnd() != null) {
            queryWrapper.le("date", query.getDateEnd());
        }
        queryWrapper.orderByDesc("date");
        queryWrapper.orderByAsc("account_id");
        return mapper.selectList(queryWrapper);
    }

    @Override
    public void create(AccountBalanceRequest request) throws ServiceException {
        var accountList = listAccount();
        var accountIdList = accountList.stream().map(AccountDTO::getId).toList();
        for (var balance : request.getBalances()) {
            if (!accountIdList.contains(balance.getAccountId())) {
                throw UserErrorCodes.REQUEST_PARAMETER_ERROR.asException(String.format("你不是账户[%d]的Owner", balance.getAccountId()));
            }
        }

        var query = new AccountBalanceQuery();
        query.setDateBegin(request.getDate());
        query.setDateEnd(request.getDate());
        var records = listBalance(accountIdList, query);
        var recordsMap = records.stream().collect(Collectors.toMap(AccountBalanceDO::getAccountId, Function.identity()));

        var createRecords = new ArrayList<AccountBalanceDO>();
        var updateRecords = new ArrayList<AccountBalanceDO>();
        for (var balance : request.getBalances()) {
            if (recordsMap.containsKey(balance.getAccountId())) {
                var record = recordsMap.get(balance.getAccountId());
                record.setBalance(balance.getBalance());
                recordsMap.remove(balance.getAccountId());
                updateRecords.add(record);
            } else {
                var record = new AccountBalanceDO();
                record.setAccountId(balance.getAccountId());
                record.setDate(request.getDate());
                record.setBalance(balance.getBalance());
                createRecords.add(record);
            }
        }
        var removeRecords = recordsMap.values().stream().map(AccountBalanceDO::getId).toList();

        if (!createRecords.isEmpty()) {
            mapper.insert(createRecords);
        }
        if (!updateRecords.isEmpty()) {
            mapper.updateById(updateRecords);
        }
        if (!removeRecords.isEmpty()) {
            mapper.deleteByIds(removeRecords);
        }
    }

}
