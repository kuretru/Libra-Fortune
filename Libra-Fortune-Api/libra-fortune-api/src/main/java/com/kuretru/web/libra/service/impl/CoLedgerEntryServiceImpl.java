package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.CoLedgerEntryDO;
import com.kuretru.web.libra.entity.data.EntryTagDO;
import com.kuretru.web.libra.entity.query.CoLedgerEntryQuery;
import com.kuretru.web.libra.entity.transfer.CoLedgerEntryDTO;
import com.kuretru.web.libra.entity.transfer.FinancialEntryDTO;
import com.kuretru.web.libra.entity.transfer.LedgerEntryDTO;
import com.kuretru.web.libra.mapper.CoLedgeEntryMapper;
import com.kuretru.web.libra.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CoLedgerEntryServiceImpl extends BaseServiceImpl<CoLedgeEntryMapper, CoLedgerEntryDO, CoLedgerEntryDTO, CoLedgerEntryQuery> implements CoLedgerEntryService {
    private final LedgerEntryService ledgerEntryService;
    private final CoLedgerUserService coLedgerUserService;
    private final FinancialEntryService financialEntryService;

    @Autowired
    public CoLedgerEntryServiceImpl(CoLedgeEntryMapper mapper, LedgerEntryService ledgerEntryService,CoLedgerUserService coLedgerUserService, FinancialEntryService financialEntryService) {
        super(mapper, CoLedgerEntryDO.class, CoLedgerEntryDTO.class, CoLedgerEntryQuery.class);
        this.ledgerEntryService = ledgerEntryService;
        this.coLedgerUserService = coLedgerUserService;
        this.financialEntryService = financialEntryService;
    }

    @Override
    public synchronized CoLedgerEntryDTO save(CoLedgerEntryDTO record) throws ServiceException {
//        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
        UUID userId = UUID.fromString("a087c0e3-2577-4a17-b435-7b12f7aa51e0");
//        UUID userId = UUID.fromString("a7f39ae9-8a75-4914-8737-3f6a979ebb92");
        LedgerEntryDTO ledgerEntry = ledgerEntryService.get(record.getEntryId());
        FinancialEntryDTO financialEntry = null;
        if (ledgerEntry == null) {
            financialEntry = financialEntryService.get(record.getEntryId());
        }
        if (financialEntry == null) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "无此条目");
        }
        record.setAmount(record.getAmount() * 10000);
        if (!userId.equals(record.getUserId()) || !coLedgerUserService.getLedgerPermission(ledgerEntry == null ? financialEntry.getLedgerId() : ledgerEntry.getLedgerId(), userId, true)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
        }
        record.setUserId(userId);
        return super.save(record);
    }

    @Override
    public CoLedgerEntryDTO  update(CoLedgerEntryDTO record) throws ServiceException {
        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
//        UUID userId = UUID.fromString("a087c0e3-2577-4a17-b435-7b12f7aa51e0");
//        UUID userId = UUID.fromString("a7f39ae9-8a75-4914-8737-3f6a979ebb92");
        CoLedgerEntryDTO oldCoLedgerEntry = get(record.getId());
        if(oldCoLedgerEntry ==null){
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "寻找的合作条目不存在");
        }
        LedgerEntryDTO ledgerEntry = ledgerEntryService.get(oldCoLedgerEntry.getEntryId());
        FinancialEntryDTO financialEntry = null;
        if (ledgerEntry != null) {
            if (!record.getEntryId().equals(oldCoLedgerEntry.getEntryId()) || !record.getUserId().equals(oldCoLedgerEntry.getUserId())) {
                throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "条目与用户不可修改");
            }
        } else {
            financialEntry = financialEntryService.get(oldCoLedgerEntry.getEntryId());
            if (financialEntry == null) {
                throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "无此条目");
            } else {
                if (!record.getEntryId().equals(oldCoLedgerEntry.getEntryId()) || !record.getUserId().equals(oldCoLedgerEntry.getUserId())) {
                    throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "条目与用户不可修改");
                }
            }
        }

        if (!userId.equals(record.getUserId()) || !coLedgerUserService.getLedgerPermission(ledgerEntry == null ? financialEntry.getLedgerId() : ledgerEntry.getLedgerId(), userId, true)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
        }
        record.setAmount(record.getAmount() * 10000);
        return super.update(record);
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

    @Override
    public Boolean getCoLedgerEntryExist(UUID userId, UUID entryId) {
        QueryWrapper<CoLedgerEntryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId.toString());
        queryWrapper.eq("entry_id", entryId.toString());
        return mapper.exists(queryWrapper);
    }
}