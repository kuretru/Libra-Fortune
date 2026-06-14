package com.kuretru.web.libra.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.context.CurrentUserContext;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.v2.service.impl.BaseSequencedServiceImpl;
import com.kuretru.web.libra.account.entity.data.AccountDO;
import com.kuretru.web.libra.account.entity.mapper.AccountEntityMapper;
import com.kuretru.web.libra.account.entity.query.AccountQuery;
import com.kuretru.web.libra.account.entity.transfer.AccountDTO;
import com.kuretru.web.libra.account.mapper.AccountMapper;
import com.kuretru.web.libra.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl
        extends BaseSequencedServiceImpl<AccountMapper, AccountDO, AccountDTO, AccountQuery>
        implements AccountService {

    @Autowired
    public AccountServiceImpl(AccountMapper mapper, AccountEntityMapper entityMapper) {
        super(mapper, entityMapper);
    }

    @Override
    public int getMaxSequence(AccountDTO record) {
        var queryWrapper = new QueryWrapper<AccountDO>();
        queryWrapper.eq("owner", record.getOwner());
        Integer result = mapper.getMaxSequence(queryWrapper);
        return null == result ? 0 : result + 1;
    }

    @Override
    protected AccountDO findDuplicateRecord(AccountDTO record) {
        var queryWrapper = new QueryWrapper<AccountDO>();
        queryWrapper.eq("owner", record.getOwner());
        queryWrapper.eq("name", record.getName());
        return mapper.selectOne(queryWrapper);
    }

    @Override
    protected QueryWrapper<AccountDO> beforeList(AccountQuery query) throws ServiceException {
        query.setOwner(CurrentUserContext.getUsername());
        return super.beforeList(query);
    }

    @Override
    protected AccountDTO afterGet(AccountDO record) throws ServiceException {
        verifyOwner(record);
        return super.afterGet(record);
    }

    @Override
    protected AccountDO beforeSave(AccountDTO record) throws ServiceException {
        record.setOwner(CurrentUserContext.getUsername());
        return super.beforeSave(record);
    }

    @Override
    protected AccountDO beforeUpdate(AccountDTO record) throws ServiceException {
        AccountDO oldRecord = mapper.selectById(record.getId());
        verifyOwner(oldRecord);
        record.setOwner(oldRecord.getOwner());
        return super.beforeUpdate(record);
    }

    @Override
    protected AccountDO beforeRemove(Long id) throws ServiceException {
        AccountDO record = super.beforeRemove(id);
        verifyOwner(record);
        return record;
    }

    private void verifyOwner(AccountDO record) {
        if (!CurrentUserContext.getUsername().equals(record.getOwner())) {
            throw ServiceException.build(UserErrorCodes.ACCESS_PERMISSION_ERROR, "仅能访问自己的账户");
        }
    }

    @Override
    public void verifyOwner(Long id) {
        var record = mapper.selectById(id);
        verifyOwner(record);
    }

    @Override
    public void verifyOwner(AccountDTO record) throws ServiceException {
        if (!CurrentUserContext.getUsername().equals(record.getOwner())) {
            throw ServiceException.build(UserErrorCodes.ACCESS_PERMISSION_ERROR, "仅能访问自己的账户");
        }
    }

}
