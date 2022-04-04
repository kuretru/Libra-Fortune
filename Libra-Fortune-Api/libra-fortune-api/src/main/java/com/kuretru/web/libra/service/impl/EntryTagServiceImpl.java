package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.authentication.annotaion.RequireAuthorization;
import com.kuretru.microservices.authentication.context.AccessTokenContext;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.EntryTagDO;
import com.kuretru.web.libra.entity.enums.LedgerTypeEnum;
import com.kuretru.web.libra.entity.query.EntryTagQuery;
import com.kuretru.web.libra.entity.transfer.*;
import com.kuretru.web.libra.mapper.EntryTagMapper;
import com.kuretru.web.libra.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class EntryTagServiceImpl extends BaseServiceImpl<EntryTagMapper, EntryTagDO, EntryTagDTO, EntryTagQuery> implements EntryTagService {

    private final UserTagService userTagService;
    private final FinancialEntryService financialEntryService;
    private final LedgerEntryService ledgerEntryService;
    private final LedgerService ledgerService;
    private final CoLedgerEntryService coLedgerEntryService;

    @Autowired
    public EntryTagServiceImpl(EntryTagMapper mapper, UserTagService userTagService, FinancialEntryService financialEntryService, LedgerEntryService ledgerEntryService, LedgerService ledgerService, CoLedgerEntryService coLedgerEntryService) {
        super(mapper, EntryTagDO.class, EntryTagDTO.class, EntryTagQuery.class);
        this.userTagService = userTagService;
        this.financialEntryService = financialEntryService;
        this.ledgerEntryService = ledgerEntryService;
        this.ledgerService = ledgerService;
        this.coLedgerEntryService = coLedgerEntryService;
    }


    @Override
    public synchronized EntryTagDTO save(EntryTagDTO record) throws ServiceException {
        UUID userId = AccessTokenContext.getUserId();
//        查看是否有权限添加tag
        if (!getUserEntryPermission(userId, record.getEntryId())) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "无权限添加");
        }
//                    有权限 查看userId 是否有tagId
        if (!userTagService.userExistTag(userId, record.getTagId())) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "该用户没有该Tag");
        }
//        是否重复
        QueryWrapper<EntryTagDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("entry_id", record.getEntryId().toString());
        queryWrapper.eq("tag_id", record.getTagId().toString());
        if (mapper.exists(queryWrapper)) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可重复添加");
        }
        return super.save(record);
    }

//    @Override
//    public EntryTagDTO update(EntryTagDTO record) throws ServiceException {
//        remove(record.getId());
//        return save(record);
//    }

    @Override
    public void remove(UUID uuid) throws ServiceException {
        UUID userId = AccessTokenContext.getUserId();
        EntryTagDTO oldRecord = get(uuid);
        if (oldRecord == null) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "记录不存在");
        }

//        查看是否有权限修改tag
        UserTagDTO userTag = userTagService.get(oldRecord.getTagId());
//        user 有没有权利对 entry 加tag || 这个tag是不是这个user的
        if (!getUserEntryPermission(userId, oldRecord.getEntryId()) || !userTag.getUserId().equals(userId)) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "无权限删除");
        }
        super.remove(uuid);
    }

    /**
     * 判断user 有没有权利对entry进行修改
     */
    @Override
    public boolean getUserEntryPermission(UUID userId, UUID entryId) throws ServiceException {
        LedgerEntryDTO ledgerEntry = ledgerEntryService.get(entryId);
        LedgerDTO ledger = null;
        if (ledgerEntry != null) {
            ledger = ledgerService.get(ledgerEntry.getLedgerId());
        } else {
            FinancialEntryDTO financialEntry = financialEntryService.get(entryId);
            if (financialEntry == null) {
                throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "entryId不存在");
            }
            ledger = ledgerService.get(financialEntry.getLedgerId());
        }

        if (ledger.getType().equals(LedgerTypeEnum.COMMON) || ledger.getType().equals(LedgerTypeEnum.FINANCIAL)) {
            return ledger.getOwnerId().equals(userId);
        } else if (ledger.getType().equals(LedgerTypeEnum.CO_COMMON) || ledger.getType().equals(LedgerTypeEnum.CO_FINANCIAL)) {
            return coLedgerEntryService.getCoLedgerEntryExist(userId, entryId);
        }
        return false;
    }

    @Override
    public void deleteByTagId(UUID tagId) {
        QueryWrapper<EntryTagDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag_id", tagId.toString());
        mapper.delete(queryWrapper);
    }

    @Override
    protected EntryTagDTO doToDto(EntryTagDO record) {
        EntryTagDTO result = super.doToDto(record);
        if (record != null) {
            result.setEntryId(UUID.fromString(record.getEntryId()));
            result.setTagId(UUID.fromString(record.getTagId()));
        }
        return result;
    }

    @Override
    protected EntryTagDO dtoToDo(EntryTagDTO record) {
        EntryTagDO result = super.dtoToDo(record);
        if (result != null) {
            result.setEntryId(record.getEntryId().toString());
            result.setTagId(record.getTagId().toString());
        }
        return result;
    }


}
