package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.authentication.context.AccessTokenContext;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerCategoryDO;
import com.kuretru.web.libra.entity.query.LedgerCategoryQuery;
import com.kuretru.web.libra.entity.transfer.LedgerCategoryDTO;
import com.kuretru.web.libra.mapper.LedgerCategoryMapper;
import com.kuretru.web.libra.service.CoLedgerUserService;
import com.kuretru.web.libra.service.LedgerCategoryService;
import com.kuretru.web.libra.service.LedgerEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class LedgerCategoryServiceImpl extends BaseServiceImpl<LedgerCategoryMapper, LedgerCategoryDO, LedgerCategoryDTO, LedgerCategoryQuery> implements LedgerCategoryService {

    private final CoLedgerUserService coLedgerUserService;
    private final LedgerEntryService ledgerEntryService;

    @Autowired
    public LedgerCategoryServiceImpl(LedgerCategoryMapper mapper, CoLedgerUserService coLedgerUserService, @Lazy LedgerEntryService ledgerEntryService) {
        super(mapper, LedgerCategoryDO.class, LedgerCategoryDTO.class, LedgerCategoryQuery.class);
        this.coLedgerUserService = coLedgerUserService;
        this.ledgerEntryService = ledgerEntryService;
    }

    @Override
    public synchronized LedgerCategoryDTO save(LedgerCategoryDTO record) throws ServiceException {
        UUID userId = AccessTokenContext.getUserId();
        if (!coLedgerUserService.getLedgerPermission(record.getLedgerId(), userId, true)) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "LedgerCategoryDTO.save.不可操做");
        }
//        不可重复添加
        QueryWrapper<LedgerCategoryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ledger_id", record.getLedgerId().toString());
        queryWrapper.eq("name", record.getName());
        if (mapper.exists(queryWrapper)) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "LedgerCategoryDTO.不可重复添加");
        }
        return super.save(record);
    }

    @Override
    public LedgerCategoryDTO update(LedgerCategoryDTO record) throws ServiceException {
        UUID userId = AccessTokenContext.getUserId();
        LedgerCategoryDTO oldRecord = get(record.getId());
        if (oldRecord == null) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "指定LedgerCategory不存在");
        }
        if (!record.getLedgerId().equals(oldRecord.getLedgerId())) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可修改ledgerId");
        }
        if (!coLedgerUserService.getLedgerPermission(record.getLedgerId(), userId, true)) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
        }
//        不可重复添加
        QueryWrapper<LedgerCategoryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ledger_id", record.getLedgerId().toString());
        queryWrapper.eq("name", record.getName());
        if (mapper.exists(queryWrapper)) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可重复");
        }
        return super.update(record);
    }

    //    先不管了
    @Override
    public void remove(UUID uuid) throws ServiceException {
        UUID userId = AccessTokenContext.getUserId();
        LedgerCategoryDTO oldRecord = get(uuid);
        if (oldRecord == null) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "指定LedgerCategory不存在");
        }
        if (!coLedgerUserService.getLedgerPermission(oldRecord.getLedgerId(), userId, true)) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
        }
        if (ledgerEntryService.existCategoryId(uuid)) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "请先删除账目中的此大类后再删除");
        }
        super.remove(uuid);
    }

    protected QueryWrapper<LedgerCategoryDO> buildQueryWrapper(LedgerCategoryQuery query) {
        UUID userId = AccessTokenContext.getUserId();
        if (!coLedgerUserService.getLedgerPermission(query.getLedgerId(), userId, true)) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "buildQueryWrapper.不可操做");
        }
        return super.buildQueryWrapper(query);
    }


    public LedgerCategoryDTO get(UUID uuid, UUID userId) {
        QueryWrapper<LedgerCategoryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", uuid.toString());
        LedgerCategoryDO record = mapper.selectOne(queryWrapper);
        if (record == null || !coLedgerUserService.getLedgerPermission(UUID.fromString(record.getLedgerId()), userId, true)) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "LedgerCategoryService.get.不可操做");
        }
        return doToDto(record);
    }

    @Override
    protected LedgerCategoryDTO doToDto(LedgerCategoryDO record) {
        LedgerCategoryDTO result = super.doToDto(record);
        if (record != null) {
            result.setLedgerId(UUID.fromString(record.getLedgerId()));
        }
        return result;
    }

    @Override
    protected LedgerCategoryDO dtoToDo(LedgerCategoryDTO record) {
        LedgerCategoryDO result = super.dtoToDo(record);
        if (result != null) {
            result.setLedgerId(record.getLedgerId().toString());
        }
        return result;
    }

}
