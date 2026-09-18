package com.kuretru.web.libra.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.context.CurrentUserContext;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.impl.BaseSequencedServiceImpl;
import com.kuretru.web.libra.account.entity.data.AccountDO;
import com.kuretru.web.libra.account.entity.data.AccountTransferDO;
import com.kuretru.web.libra.account.entity.mapper.AccountEntityMapper;
import com.kuretru.web.libra.account.entity.query.AccountQuery;
import com.kuretru.web.libra.account.entity.transfer.AccountDTO;
import com.kuretru.web.libra.account.mapper.AccountMapper;
import com.kuretru.web.libra.account.mapper.AccountTransferMapper;
import com.kuretru.web.libra.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl
        extends BaseSequencedServiceImpl<AccountMapper, AccountDO, AccountDTO, AccountQuery>
        implements AccountService {

    private final AccountTransferMapper accountTransferMapper;

    @Autowired
    public AccountServiceImpl(AccountMapper mapper, AccountEntityMapper entityMapper,
                              AccountTransferMapper accountTransferMapper) {
        super(mapper, entityMapper);
        this.accountTransferMapper = accountTransferMapper;
    }

    @Override
    public int getMaxSequence(AccountDTO record) {
        var queryWrapper = new QueryWrapper<AccountDO>();
        queryWrapper.eq("owner", record.getOwner());
        Integer result = mapper.getMaxSequence(queryWrapper);
        return null == result ? 0 : result;
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
    protected AccountDO beforeCreate(AccountDTO record) throws ServiceException {
        record.setOwner(CurrentUserContext.getUsername());
        return super.beforeCreate(record);
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
        var queryWrapper = new QueryWrapper<AccountTransferDO>();
        queryWrapper.eq("source_account_id", id).or().eq("target_account_id", id);
        if (accountTransferMapper.selectCount(queryWrapper) > 0) {
            throw UserErrorCodes.REQUEST_PARAMETER_ERROR.asException("账户存在转账记录，不能删除");
        }
        return record;
    }

    private void verifyOwner(AccountDO record) {
        if (record == null) {
            throw UserErrorCodes.REQUEST_PARAMETER_ERROR.asException("指定资源不存在");
        }
        if (!CurrentUserContext.getUsername().equals(record.getOwner())) {
            throw UserErrorCodes.ACCESS_PERMISSION_ERROR.asException("仅能访问自己的账户");
        }
    }

    @Override
    public void verifyOwner(Long id) {
        var record = mapper.selectById(id);
        verifyOwner(record);
    }

    @Override
    public void verifyOwner(AccountDTO record) throws ServiceException {
        if (record == null) {
            throw UserErrorCodes.REQUEST_PARAMETER_ERROR.asException("指定资源不存在");
        }
        if (!CurrentUserContext.getUsername().equals(record.getOwner())) {
            throw UserErrorCodes.ACCESS_PERMISSION_ERROR.asException("仅能访问自己的账户");
        }
    }

}
