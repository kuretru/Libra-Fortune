package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.CoLedgerEntryDO;
import com.kuretru.web.libra.entity.enums.LedgerTypeEnum;
import com.kuretru.web.libra.entity.query.CoLedgerEntryQuery;
import com.kuretru.web.libra.entity.transfer.CoLedgerEntryDTO;
import com.kuretru.web.libra.entity.transfer.FinancialEntryDTO;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.entity.transfer.LedgerEntryDTO;
import com.kuretru.web.libra.mapper.CoLedgeEntryMapper;
import com.kuretru.web.libra.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CoLedgerEntryServiceImpl extends BaseServiceImpl<CoLedgeEntryMapper, CoLedgerEntryDO, CoLedgerEntryDTO, CoLedgerEntryQuery> implements CoLedgerEntryService {
    private final LedgerEntryService ledgerEntryService;
    private final CoLedgerUserService coLedgerUserService;
    private final FinancialEntryService financialEntryService;
    private final LedgerService ledgerService;

    @Autowired
    public CoLedgerEntryServiceImpl(CoLedgeEntryMapper mapper, LedgerEntryService ledgerEntryService, CoLedgerUserService coLedgerUserService, FinancialEntryService financialEntryService, LedgerService ledgerService) {
        super(mapper, CoLedgerEntryDO.class, CoLedgerEntryDTO.class, CoLedgerEntryQuery.class);
        this.ledgerEntryService = ledgerEntryService;
        this.coLedgerUserService = coLedgerUserService;
        this.financialEntryService = financialEntryService;
        this.ledgerService = ledgerService;
    }

    @Override
    public CoLedgerEntryDTO save(String ledgerId, CoLedgerEntryDTO record) throws ServiceException {
//        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
//        UUID userId = UUID.fromString("a087c0e3-2577-4a17-b435-7b12f7aa51e0");
        UUID userId = UUID.fromString("a7f39ae9-8a75-4914-8737-3f6a979ebb92");
        //        是否重复
        QueryWrapper<CoLedgerEntryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("entry_id", record.getEntryId().toString());
        queryWrapper.eq("user_id", userId.toString());
        if (mapper.exists(queryWrapper)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可重复添加");
        }

        LedgerDTO existLedger = ledgerService.get(UUID.fromString(ledgerId));
        if (existLedger == null) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "该账本不存在");
        }
//        如果账本是普通合作账本
        if (existLedger.getType().equals(LedgerTypeEnum.CO_COMMON)) {
//            去查entryId 对应的 ledgerEntry
            LedgerEntryDTO ledgerEntry = ledgerEntryService.get(record.getEntryId());
//            如果查到的ledgerEntry的账本和传过来的账本id不同
            if (ledgerEntry == null) {
                throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "该entry不存在");
            }
            if (!ledgerEntry.getLedgerId().toString().equals(ledgerId)) {
                throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "条目的ledgerId与url上的ledgerId不同");
            }
        } else if (existLedger.getType().equals(LedgerTypeEnum.CO_FINANCIAL)) {
//            去查entryId 对应的 ledgerEntry
            FinancialEntryDTO financialEntry = financialEntryService.get(record.getEntryId());
//            如果查到的ledgerEntry的账本和传过来的账本id不同
            if (financialEntry == null) {
                throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "该entry不存在");
            }
            if (!financialEntry.getLedgerId().toString().equals(ledgerId)) {
                throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "条目的ledgerId与url上的ledgerId不同");
            }
        } else {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账本类型错误");
        }

        if (!coLedgerUserService.getLedgerPermission(UUID.fromString(ledgerId), userId, true)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "无权添加");
        }
        record.setUserId(userId);
        return super.save(record);
    }




    @Override
    public CoLedgerEntryDTO update(CoLedgerEntryDTO record) throws ServiceException {
//        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
        UUID userId = UUID.fromString("a087c0e3-2577-4a17-b435-7b12f7aa51e0");
//        UUID userId = UUID.fromString("a7f39ae9-8a75-4914-8737-3f6a979ebb92");
        CoLedgerEntryDTO oldCoLedgerEntry = get(record.getId());
        if (oldCoLedgerEntry == null) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "寻找的合作条目不存在");
        }

        if (!record.getEntryId().equals(oldCoLedgerEntry.getEntryId()) || !record.getUserId().equals(oldCoLedgerEntry.getUserId())) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "条目与用户不可修改");
        }

        LedgerEntryDTO ledgerEntry = ledgerEntryService.get(oldCoLedgerEntry.getEntryId());
        FinancialEntryDTO financialEntry = null;

        if (ledgerEntry == null) {
            financialEntry = financialEntryService.get(oldCoLedgerEntry.getEntryId());
        }

        if (!userId.equals(record.getUserId()) || !coLedgerUserService.getLedgerPermission(ledgerEntry == null ? financialEntry.getLedgerId() : ledgerEntry.getLedgerId(), userId, true)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
        }
        return super.update(record);
    }

    @Override
    public void remove(UUID uuid) throws ServiceException {
//        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
        UUID userId = UUID.fromString("a087c0e3-2577-4a17-b435-7b12f7aa51e0");
//        UUID userId = UUID.fromString("a7f39ae9-8a75-4914-8737-3f6a979ebb92");
        CoLedgerEntryDTO oldCoLedgerEntry = get(uuid);
        if (oldCoLedgerEntry == null) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "寻找的合作条目不存在");
        }

        LedgerEntryDTO ledgerEntry = ledgerEntryService.get(oldCoLedgerEntry.getEntryId());
        FinancialEntryDTO financialEntry = null;

        if (ledgerEntry == null) {
            financialEntry = financialEntryService.get(oldCoLedgerEntry.getEntryId());
        }

        if (!userId.equals(oldCoLedgerEntry.getUserId()) || !coLedgerUserService.getLedgerPermission(ledgerEntry == null ? financialEntry.getLedgerId() : ledgerEntry.getLedgerId(), userId, true)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
        }
        super.remove(uuid);
    }

    @Override
    public void deleteByEntryId(UUID tagId) {
        QueryWrapper<CoLedgerEntryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("entry_id", tagId.toString());
        mapper.delete(queryWrapper);
    }

    @Override
    public Boolean getCoLedgerEntryExist(UUID userId, UUID entryId) {
        QueryWrapper<CoLedgerEntryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId.toString());
        queryWrapper.eq("entry_id", entryId.toString());
        return mapper.exists(queryWrapper);
    }

    @Override
    protected CoLedgerEntryDTO doToDto(CoLedgerEntryDO record) {
        CoLedgerEntryDTO result = super.doToDto(record);
        if (record != null) {
            result.setUserId(UUID.fromString(record.getUserId()));
            result.setEntryId(UUID.fromString(record.getEntryId()));
        }
        return result;
    }

    @Override
    protected CoLedgerEntryDO dtoToDo(CoLedgerEntryDTO record) {
        CoLedgerEntryDO result = super.dtoToDo(record);
        if (result != null) {
            result.setEntryId(record.getEntryId().toString());
            result.setUserId(record.getUserId().toString());
        }
        return result;
    }


}