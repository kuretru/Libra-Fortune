package com.kuretru.web.libra.service.impl;

import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.CoLedgerEntryDO;
import com.kuretru.web.libra.entity.enums.LedgerTypeEnum;
import com.kuretru.web.libra.entity.query.CoLedgerEntryQuery;
import com.kuretru.web.libra.entity.transfer.CoLedgerEntryDTO;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.entity.transfer.LedgerEntryDTO;
import com.kuretru.web.libra.mapper.CoLedgeEntryMapper;
import com.kuretru.web.libra.service.CoLedgerEntryService;
import com.kuretru.web.libra.service.CoLedgerUserService;
import com.kuretru.web.libra.service.LedgerEntryService;
import com.kuretru.web.libra.service.LedgerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CoLedgerEntryImpl extends BaseServiceImpl<CoLedgeEntryMapper, CoLedgerEntryDO, CoLedgerEntryDTO, CoLedgerEntryQuery> implements CoLedgerEntryService {
    private final LedgerEntryService ledgerEntryService;
    private final LedgerService ledgerService;
    private final CoLedgerUserService coLedgerUserService;

    @Autowired
    public CoLedgerEntryImpl(CoLedgeEntryMapper mapper, LedgerEntryService ledgerEntryService, LedgerService ledgerService, CoLedgerUserService coLedgerUserService) {
        super(mapper, CoLedgerEntryDO.class, CoLedgerEntryDTO.class, CoLedgerEntryQuery.class);
        this.ledgerEntryService = ledgerEntryService;
        this.ledgerService = ledgerService;
        this.coLedgerUserService = coLedgerUserService;
    }

    @Override
    public synchronized CoLedgerEntryDTO save(CoLedgerEntryDTO record) throws ServiceException {
//        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
        UUID userId = UUID.fromString("a087c0e3-2577-4a17-b435-7b12f7aa51e0");
//        UUID userId = UUID.fromString("a7f39ae9-8a75-4914-8737-3f6a979ebb92");
        LedgerEntryDTO ledgerEntry = ledgerEntryService.get(record.getEntryId());
        if (ledgerEntry == null) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "无此条目");
        }
        LedgerDTO ledger = ledgerService.get(ledgerEntry.getLedgerId());
        if (ledger.getType().equals(LedgerTypeEnum.CO_COMMON)) {
            if (!userId.equals(record.getUserId()) || !coLedgerUserService.getLedgerPermission(ledgerEntry.getLedgerId(), userId, true)) {
                throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
            }
            record.setAmount(record.getAmount() * 10000);
            record.setUserId(userId);
            return super.save(record);
        }
        return null;
    }

    @Override
    public CoLedgerEntryDTO update(CoLedgerEntryDTO record) throws ServiceException {
        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
//        UUID userId = UUID.fromString("a087c0e3-2577-4a17-b435-7b12f7aa51e0");
//        UUID userId = UUID.fromString("a7f39ae9-8a75-4914-8737-3f6a979ebb92");
        CoLedgerEntryDTO oldLedgerEntry = get(record.getId());
        LedgerEntryDTO ledgerEntry = ledgerEntryService.get(record.getEntryId());
        if (ledgerEntry == null || oldLedgerEntry == null) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "无此条目");
        }
        LedgerDTO ledger = ledgerService.get(ledgerEntry.getLedgerId());
//       更改合作人和属于的条目
        if (!record.getEntryId().equals(oldLedgerEntry.getEntryId()) || !record.getUserId().equals(oldLedgerEntry.getUserId())) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "条目与用户不可修改");

        }
//        如果是普通的合作账本
        if (ledger.getType().equals(LedgerTypeEnum.CO_COMMON)) {
//            修改用户不为当前登录用户，或者当前登录用户对此账本无修改权
            if (!userId.equals(record.getUserId()) || !coLedgerUserService.getLedgerPermission(ledgerEntry.getLedgerId(), userId, true)) {
                throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
            }
            record.setAmount(record.getAmount() * 10000);
            record.setUserId(userId);
            return super.update(record);
        }
        return null;
    }

//    @Override
//    public void remove(UUID uuid) throws ServiceException {
//        super.remove(uuid);
//    }

    @Override
    protected CoLedgerEntryDTO doToDto(CoLedgerEntryDO record) {
        if (record == null) {
            return null;
        }
        CoLedgerEntryDTO result = buildDTOInstance();
        BeanUtils.copyProperties(record, result);
        result.setId(UUID.fromString(record.getUuid()));
        result.setUserId(UUID.fromString(record.getUserId()));
        result.setEntryId(UUID.fromString(record.getEntryId()));
        return result;
    }

    @Override
    protected CoLedgerEntryDO dtoToDo(CoLedgerEntryDTO record) {
        if (record == null) {
            return null;
        }
        CoLedgerEntryDO result = buildDOInstance();
        BeanUtils.copyProperties(record, result);
        if (record.getId() != null) {
            result.setUuid(record.getId().toString());
        }
        result.setEntryId(record.getEntryId().toString());
        result.setUserId(record.getUserId().toString());
        return result;
    }


}