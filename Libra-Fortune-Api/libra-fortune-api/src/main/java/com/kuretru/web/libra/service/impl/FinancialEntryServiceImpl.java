package com.kuretru.web.libra.service.impl;

import com.kuretru.microservices.authentication.annotaion.RequireAuthorization;
import com.kuretru.microservices.authentication.context.AccessTokenContext;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.FinancialEntryDO;
import com.kuretru.web.libra.entity.enums.LedgerTypeEnum;
import com.kuretru.web.libra.entity.query.FinancialEntryQuery;
import com.kuretru.web.libra.entity.transfer.FinancialEntryDTO;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.mapper.FinancialEntryMapper;
import com.kuretru.web.libra.service.CoLedgerEntryService;
import com.kuretru.web.libra.service.CoLedgerUserService;
import com.kuretru.web.libra.service.FinancialEntryService;
import com.kuretru.web.libra.service.LedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class FinancialEntryServiceImpl extends BaseServiceImpl<FinancialEntryMapper, FinancialEntryDO, FinancialEntryDTO, FinancialEntryQuery> implements FinancialEntryService {

    private final LedgerService ledgerService;
    private final CoLedgerUserService coLedgerUserService;
    private final CoLedgerEntryService coLedgerEntryService;

    @Autowired
    public FinancialEntryServiceImpl(FinancialEntryMapper mapper, LedgerService ledgerService, CoLedgerUserService coLedgerUserService, @Lazy CoLedgerEntryService coLedgerEntryService) {
        super(mapper, FinancialEntryDO.class, FinancialEntryDTO.class, FinancialEntryQuery.class);
        this.ledgerService = ledgerService;
        this.coLedgerUserService = coLedgerUserService;
        this.coLedgerEntryService = coLedgerEntryService;
    }

    @Override
    public synchronized FinancialEntryDTO save(FinancialEntryDTO record) throws ServiceException {
        UUID userId = AccessTokenContext.getUserId();
        LedgerDTO existLedger = ledgerService.get(record.getLedgerId());
        if (existLedger == null) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "该账本不存在");
        }

//        个人普通账本/合作普通账本：如果账本的拥有者不为当前用户
        if (existLedger.getType().equals(LedgerTypeEnum.FINANCIAL)) {
            if (!existLedger.getOwnerId().equals(userId)) {
                throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操作");
            }
        } else if (existLedger.getType().equals(LedgerTypeEnum.CO_FINANCIAL)) {
            if (!coLedgerUserService.getLedgerPermission(existLedger.getId(), userId, true)) {
                throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操作");
            }
        } else {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账本类型错误");
        }
        return super.save(record);
    }

    @Override
    public FinancialEntryDTO update(FinancialEntryDTO record) throws ServiceException {
        UUID userId = AccessTokenContext.getUserId();
//       判断账目存在
        FinancialEntryDTO oldRecord = get(record.getId());
//        账目不存在
        if (oldRecord == null) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "该账目不存在");
        }

        if (!oldRecord.getLedgerId().equals(record.getLedgerId())) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "属于账本不可变");
        }

        LedgerDTO existLedger = ledgerService.get(record.getLedgerId());
        if (existLedger.getType().equals(LedgerTypeEnum.FINANCIAL)) {
            if (!existLedger.getOwnerId().equals(userId)) {
                throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操作");
            }
        } else if (existLedger.getType().equals(LedgerTypeEnum.CO_FINANCIAL)) {
            if (!coLedgerUserService.getLedgerPermission(existLedger.getId(), userId, true)) {
                throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操作");
            }
        } else {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账本类型错误");
        }
        return super.update(record);
    }

    @Override
    public void remove(UUID uuid) throws ServiceException {
        UUID userId = AccessTokenContext.getUserId();
        FinancialEntryDTO oldLedgerEntry = get(uuid);
        if (oldLedgerEntry == null) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账目不存在");
        }

//       找到账本
        LedgerDTO existLedger = ledgerService.get(oldLedgerEntry.getLedgerId());
//        个人普通账本/合作普通账本：如果账本的拥有者不为当前用户
        if (existLedger.getType().equals(LedgerTypeEnum.FINANCIAL)) {
            if (!existLedger.getOwnerId().equals(userId)) {
                throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操作");
            }
        } else if (existLedger.getType().equals(LedgerTypeEnum.CO_FINANCIAL)) {
            if (!coLedgerUserService.getLedgerPermission(existLedger.getId(), userId, true)) {
                throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操作");
            }
        } else {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账本类型错误");
        }

        super.remove(uuid);
        coLedgerEntryService.deleteByEntryId(uuid);
    }


    @Override
    protected FinancialEntryDTO doToDto(FinancialEntryDO record) {
        FinancialEntryDTO result = super.doToDto(record);
        if (record != null) {
            result.setLedgerId(UUID.fromString(record.getLedgerId()));
        }
        return result;
    }

    @Override
    protected FinancialEntryDO dtoToDo(FinancialEntryDTO record) {
        FinancialEntryDO result = super.dtoToDo(record);
        if (result != null) {
            result.setLedgerId(record.getLedgerId().toString());
        }
        return result;
    }

}
