package com.kuretru.web.libra.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.common.entity.enums.EnumDTO;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.context.CurrentUserContext;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.account.entity.data.AccountTransferDO;
import com.kuretru.web.libra.account.entity.mapper.AccountTransferEntityMapper;
import com.kuretru.web.libra.account.entity.query.AccountTransferQuery;
import com.kuretru.web.libra.account.entity.transfer.AccountDTO;
import com.kuretru.web.libra.account.entity.transfer.AccountTransferDTO;
import com.kuretru.web.libra.account.mapper.AccountTransferMapper;
import com.kuretru.web.libra.account.service.AccountService;
import com.kuretru.web.libra.account.service.AccountTransferService;
import com.kuretru.web.libra.metadata.service.MetadataCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AccountTransferServiceImpl
        extends BaseServiceImpl<AccountTransferMapper, AccountTransferDO, AccountTransferDTO, AccountTransferQuery>
        implements AccountTransferService {

    private final AccountService accountService;
    private final MetadataCurrencyService currencyService;

    @Autowired
    public AccountTransferServiceImpl(AccountTransferMapper mapper, AccountTransferEntityMapper entityMapper,
                                      AccountService accountService, MetadataCurrencyService currencyService) {
        super(mapper, entityMapper);
        this.accountService = accountService;
        this.currencyService = currencyService;
    }

    @Override
    protected void applyDefaultOrderBy(QueryWrapper<AccountTransferDO> queryWrapper) {
        queryWrapper.orderByDesc("date");
        queryWrapper.orderByDesc("create_time");
        queryWrapper.orderByDesc("id");
    }

    @Override
    protected AccountTransferDTO afterGet(AccountTransferDO record) throws ServiceException {
        verifyOwner(record);
        return super.afterGet(record);
    }

    @Override
    protected QueryWrapper<AccountTransferDO> beforeList(AccountTransferQuery query) throws ServiceException {
        query.setOwner(CurrentUserContext.getUsername());
        return super.beforeList(query);
    }

    @Override
    protected AccountTransferDO beforeCreate(AccountTransferDTO record) throws ServiceException {
        record.setOwner(CurrentUserContext.getUsername());
        verifyDTO(record);
        return super.beforeCreate(record);
    }

    @Override
    protected AccountTransferDO beforeUpdate(AccountTransferDTO record) throws ServiceException {
        var oldRecord = mapper.selectById(record.getId());
        verifyOwner(oldRecord);
        record.setOwner(oldRecord.getOwner());
        verifyDTO(record);
        return super.beforeUpdate(record);
    }

    @Override
    protected AccountTransferDO beforeRemove(Long id) throws ServiceException {
        var record = super.beforeRemove(id);
        verifyOwner(record);
        return record;
    }

    private void verifyDTO(AccountTransferDTO record) {
        if (record.getDate().isAfter(LocalDate.now())) {
            throw UserErrorCodes.REQUEST_PARAMETER_ERROR.asException("不能添加今天之后的转账记录");
        }

        var sourceAccount = accountService.get(record.getSourceAccountId());
        verifyCanHoldFunds(sourceAccount);
        var targetAccount = record.getSourceAccountId().equals(record.getTargetAccountId())
                ? sourceAccount
                : accountService.get(record.getTargetAccountId());
        verifyCanHoldFunds(targetAccount);

        var currencies = currencyService.enums().stream().map(EnumDTO::getValue).toList();
        if (!currencies.contains(record.getSourceCurrency())) {
            throw UserErrorCodes.REQUEST_PARAMETER_ERROR.asException("转出货币类型不合法");
        }
        if (!currencies.contains(record.getTargetCurrency())) {
            throw UserErrorCodes.REQUEST_PARAMETER_ERROR.asException("转入货币类型不合法");
        }
        if (record.getSourceAccountId().equals(record.getTargetAccountId())
                && record.getSourceCurrency().equals(record.getTargetCurrency())) {
            throw UserErrorCodes.REQUEST_PARAMETER_ERROR.asException("相同账户仅允许不同货币之间换汇");
        }
    }

    private void verifyCanHoldFunds(AccountDTO account) {
        if (!account.getCanHoldFunds()) {
            throw UserErrorCodes.REQUEST_PARAMETER_ERROR.asException("转账账户必须允许储蓄");
        }
    }

    private void verifyOwner(AccountTransferDO record) {
        if (record == null) {
            throw UserErrorCodes.REQUEST_PARAMETER_ERROR.asException("指定资源不存在");
        }
        if (!CurrentUserContext.getUsername().equals(record.getOwner())) {
            throw UserErrorCodes.ACCESS_PERMISSION_ERROR.asException("仅能访问自己的转账记录");
        }
    }

}
