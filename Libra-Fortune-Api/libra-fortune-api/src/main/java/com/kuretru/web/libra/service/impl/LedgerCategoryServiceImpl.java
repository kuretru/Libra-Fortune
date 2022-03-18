package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerCategoryDO;
import com.kuretru.web.libra.entity.enums.LedgerTypeEnum;
import com.kuretru.web.libra.entity.query.LedgerCategoryQuery;
import com.kuretru.web.libra.entity.transfer.LedgerCategoryDTO;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.mapper.LedgerCategoryMapper;
import com.kuretru.web.libra.service.CoLedgerUserService;
import com.kuretru.web.libra.service.LedgerCategoryService;
import com.kuretru.web.libra.service.LedgerEntryService;
import com.kuretru.web.libra.service.LedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LedgerCategoryServiceImpl extends BaseServiceImpl<LedgerCategoryMapper, LedgerCategoryDO, LedgerCategoryDTO, LedgerCategoryQuery> implements LedgerCategoryService {
    private final LedgerService ledgerService;
    private final CoLedgerUserService coLedgerUserService;
    private final LedgerEntryService ledgerEntryService;

    @Autowired
    public LedgerCategoryServiceImpl(LedgerCategoryMapper mapper, LedgerService ledgerService, CoLedgerUserService coLedgerUserService, @Lazy LedgerEntryService ledgerEntryService) {
        super(mapper, LedgerCategoryDO.class, LedgerCategoryDTO.class, LedgerCategoryQuery.class);
        this.ledgerService = ledgerService;
        this.coLedgerUserService = coLedgerUserService;
        this.ledgerEntryService = ledgerEntryService;
    }

    @Override
    public synchronized LedgerCategoryDTO save(LedgerCategoryDTO record) throws ServiceException {
//      UUID userId = UUID.fromString("a087c0e3-2577-4a17-b435-7b12f7aa51e0");
        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
//        UUID userId = UUID.fromString("a7f39ae9-8a75-4914-8737-3f6a979ebb92");
        LedgerDTO existLedger = ledgerService.get(record.getLedgerId());
        if (existLedger == null) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "ledger不存在");
        }
//        不可重复添加
        QueryWrapper<LedgerCategoryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ledger_id", record.getLedgerId().toString());
        queryWrapper.eq("name", record.getName());
        if (mapper.exists(queryWrapper)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可重复添加");
        }

//        if (existLedger.getType().equals(LedgerTypeEnum.COMMON) || existLedger.getType().equals(LedgerTypeEnum.FINANCIAL)) {
        if (existLedger.getType().equals(LedgerTypeEnum.COMMON)) {
//        单人普通/理财账本：如果账本的拥有者不为当前用户
            if (!existLedger.getOwnerId().equals(userId)) {
                throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
            }
        } else if (existLedger.getType().equals(LedgerTypeEnum.CO_COMMON)) {
//            如果是合作普通/理财账本 用coLedgerUserService
//            查询co_ledger_user ledger_id +userId + is_writable = 1;
//            如果查不到报错
            if (!coLedgerUserService.getLedgerPermission(existLedger.getId(), userId, true)) {
                throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
            }
        }else{
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账本类型不对");
        }
        return super.save(record);
    }

    @Override
    public LedgerCategoryDTO update(LedgerCategoryDTO record) throws ServiceException {
//          UUID userId = UUID.fromString("a087c0e3-2577-4a17-b435-7b12f7aa51e0");
        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
//        UUID userId = UUID.fromString("a7f39ae9-8a75-4914-8737-3f6a979ebb92");
        LedgerCategoryDTO oldRecord = get(record.getId());
        if (oldRecord == null) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "指定LedgerCategory不存在");
        }

        if (!record.getLedgerId().equals(oldRecord.getLedgerId())) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可修改ledgerId");
        }
        LedgerDTO existLedger = ledgerService.get(record.getLedgerId());

//        查看账本是否是合作账本
        if (existLedger.getType().equals(LedgerTypeEnum.COMMON)) {
//        单人普通/理财账本：如果账本的拥有者不为当前用户
            if (!existLedger.getOwnerId().equals(userId)) {
                throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
            }
        } else if (existLedger.getType().equals(LedgerTypeEnum.CO_COMMON)) {
//            如果是合作普通/理财账本 用coLedgerUserService
//            查询co_ledger_user ledger_id +userId + is_writable = 1;
//            如果查不到报错
            if (!coLedgerUserService.getLedgerPermission(existLedger.getId(), userId, true)) {
                throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
            }
        }else{
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账本类型不对");
        }
        //        不可重复添加
        QueryWrapper<LedgerCategoryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ledger_id", record.getLedgerId().toString());
        queryWrapper.eq("name", record.getName());
        if (mapper.exists(queryWrapper)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可重复");
        }
        return super.update(record);
    }

//    先不管了
    @Override
    public void remove(UUID uuid) throws ServiceException {
//        UUID userId = UUID.fromString("a087c0e3-2577-4a17-b435-7b12f7aa51e0");
        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
//        UUID userId = UUID.fromString("a7f39ae9-8a75-4914-8737-3f6a979ebb92");
        LedgerCategoryDTO oldRecord = get(uuid);
        if (oldRecord == null) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "指定LedgerCategory不存在");
        }

        LedgerDTO existLedger = ledgerService.get(oldRecord.getLedgerId());

//        查看账本是否是合作账本
        if (existLedger.getType().equals(LedgerTypeEnum.COMMON)) {
//        单人普通/理财账本：如果账本的拥有者不为当前用户
            if (!existLedger.getOwnerId().equals(userId)) {
                throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
            }
        } else if (existLedger.getType().equals(LedgerTypeEnum.CO_COMMON)) {
//            如果是合作普通/理财账本 用coLedgerUserService
//            查询co_ledger_user ledger_id +userId + is_writable = 1;
//            如果查不到报错
            if (!coLedgerUserService.getLedgerPermission(existLedger.getId(), userId, true)) {
                throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
            }
        }else{
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账本类型不对");
        }
        if(ledgerEntryService.existCategoryId(uuid)){
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "请先删除账目中的此大类后再删除");
        }
        super.remove(uuid);
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