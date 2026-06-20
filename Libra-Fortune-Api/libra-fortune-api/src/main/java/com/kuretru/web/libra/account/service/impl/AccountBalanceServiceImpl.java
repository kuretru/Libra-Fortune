package com.kuretru.web.libra.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.context.CurrentUserContext;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.v2.service.ability.children.ChildrenOperator;
import com.kuretru.microservices.web.v2.service.ability.children.DefaultChildrenOperator;
import com.kuretru.microservices.web.v2.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.account.entity.data.AccountBalanceDO;
import com.kuretru.web.libra.account.entity.mapper.AccountBalanceEntityMapper;
import com.kuretru.web.libra.account.entity.query.AccountBalanceQuery;
import com.kuretru.web.libra.account.entity.query.AccountQuery;
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

    private final ChildrenOperator<AccountBalanceDTO, AccountBalanceQuery> childrenOperator;
    private final AccountService accountService;

    @Autowired
    public AccountBalanceServiceImpl(AccountBalanceMapper mapper, AccountBalanceEntityMapper entityMapper,
                                     AccountService accountService) {
        super(mapper, entityMapper);
        this.childrenOperator = new DefaultChildrenOperator<>(mapper, entityMapper,
                AccountBalanceDO.class, AccountBalanceDTO.class, AccountBalanceQuery.class);
        this.accountService = accountService;
    }

    @Override
    public ChildrenOperator<AccountBalanceDTO, AccountBalanceQuery> childrenOperator() {
        return childrenOperator;
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
        var accountQuery = new AccountQuery();
        accountQuery.setOwner(CurrentUserContext.getUsername());
        accountQuery.setCanHoldFunds(true);
        var accountIdList = accountService.listId(accountQuery);
        query.setAccountIdIn(accountIdList);
        return super.beforeList(query);
    }

    @Override
    protected AccountBalanceDTO afterGet(AccountBalanceDO record) throws ServiceException {
        accountService.verifyOwner(record.getAccountId());
        return super.afterGet(record);
    }

    @Override
    protected AccountBalanceDO beforeSave(AccountBalanceDTO record) throws ServiceException {
        verifyOwnerAndCanHoldFunds(record.getAccountId());
        return super.beforeSave(record);
    }

    @Override
    protected AccountBalanceDO beforeUpdate(AccountBalanceDTO record) throws ServiceException {
        var oldRecord = mapper.selectById(record.getId());
        verifyOwnerAndCanHoldFunds(oldRecord.getAccountId());
        record.setAccountId(oldRecord.getAccountId());
        return super.beforeUpdate(record);
    }

    @Override
    protected AccountBalanceDO beforeRemove(Long id) throws ServiceException {
        var record = super.beforeRemove(id);
        accountService.verifyOwner(record.getAccountId());
        return super.beforeRemove(id);
    }

    private void verifyOwnerAndCanHoldFunds(Long accountId) throws ServiceException {
        var account = accountService.get(accountId);
        accountService.verifyOwner(accountId);
        if (!account.getCanHoldFunds()) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "该账户不允许储蓄");
        }
    }

}
