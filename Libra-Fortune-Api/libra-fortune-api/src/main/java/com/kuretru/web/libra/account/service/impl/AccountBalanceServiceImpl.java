package com.kuretru.web.libra.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.v2.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.account.entity.data.AccountBalanceDO;
import com.kuretru.web.libra.account.entity.mapper.AccountBalanceEntityMapper;
import com.kuretru.web.libra.account.entity.query.AccountBalanceQuery;
import com.kuretru.web.libra.account.entity.transfer.AccountBalanceDTO;
import com.kuretru.web.libra.account.mapper.AccountBalanceMapper;
import com.kuretru.web.libra.account.service.AccountBalanceService;
import com.kuretru.web.libra.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountBalanceServiceImpl
        extends BaseServiceImpl<AccountBalanceMapper, AccountBalanceDO, AccountBalanceDTO, AccountBalanceQuery>
        implements AccountBalanceService {

    private final AccountService accountService;

    @Autowired
    public AccountBalanceServiceImpl(AccountBalanceMapper mapper, AccountBalanceEntityMapper entityMapper,
                                     AccountService accountService) {
        super(mapper, entityMapper);
        this.accountService = accountService;
    }

    @Override
    protected AccountBalanceDO findDuplicateRecord(AccountBalanceDTO record) {
        var queryWrapper = new QueryWrapper<AccountBalanceDO>();
        queryWrapper.eq("account_id", record.getAccountId());
        queryWrapper.eq("date", record.getDate());
        return mapper.selectOne(queryWrapper);
    }

    @Override
    protected QueryWrapper<AccountBalanceDO> beforeList(AccountBalanceQuery query) throws ServiceException {
        accountService.verifyOwner(query.getAccountId());
        return super.beforeList(query);
    }

    @Override
    protected AccountBalanceDTO afterGet(AccountBalanceDO record) throws ServiceException {
        accountService.verifyOwner(record.getAccountId());
        return super.afterGet(record);
    }

    @Override
    protected AccountBalanceDO beforeSave(AccountBalanceDTO record) throws ServiceException {
        accountService.verifyOwner(record.getAccountId());
        return super.beforeSave(record);
    }

    @Override
    protected AccountBalanceDO beforeUpdate(AccountBalanceDTO record) throws ServiceException {
        var oldRecord = mapper.selectById(record.getId());
        accountService.verifyOwner(oldRecord.getAccountId());
        record.setAccountId(oldRecord.getAccountId());
        return super.beforeUpdate(record);
    }

    @Override
    protected AccountBalanceDO beforeRemove(Long id) throws ServiceException {
        var record = super.beforeRemove(id);
        accountService.verifyOwner(record.getAccountId());
        return super.beforeRemove(id);
    }

}
