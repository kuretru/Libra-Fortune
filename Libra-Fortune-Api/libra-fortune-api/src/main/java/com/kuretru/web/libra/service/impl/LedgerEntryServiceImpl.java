package com.kuretru.web.libra.service.impl;

import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerEntryDO;
import com.kuretru.web.libra.entity.enums.LedgerTypeEnum;
import com.kuretru.web.libra.entity.query.LedgerEntryQuery;
import com.kuretru.web.libra.entity.transfer.LedgerCategoryDTO;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.entity.transfer.LedgerEntryDTO;
import com.kuretru.web.libra.mapper.LedgerEntryMapper;
import com.kuretru.web.libra.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LedgerEntryImpl extends BaseServiceImpl<LedgerEntryMapper, LedgerEntryDO, LedgerEntryDTO, LedgerEntryQuery> implements LedgerEntryService {
    private final LedgerService ledgerService;
    private final LedgerCategoryService ledgerCategoryService;
    private final SystemUserService systemUserService;
    private final CoLedgerUserService coLedgerUserService;

    @Autowired
    public LedgerEntryImpl(LedgerEntryMapper mapper, LedgerService ledgerService, LedgerCategoryService ledgerCategoryService, SystemUserService systemUserService, CoLedgerUserService coLedgerUserService) {
        super(mapper, LedgerEntryDO.class, LedgerEntryDTO.class, LedgerEntryQuery.class);
        this.ledgerService = ledgerService;
        this.ledgerCategoryService = ledgerCategoryService;
        this.systemUserService = systemUserService;
        this.coLedgerUserService = coLedgerUserService;
    }

    @Override
    public synchronized LedgerEntryDTO save(LedgerEntryDTO record) throws ServiceException {
//        UUID userId = UUID.fromString("a087c0e3-2577-4a17-b435-7b12f7aa51e0");
        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
//        UUID userId = UUID.fromString("a7f39ae9-8a75-4914-8737-3f6a979ebb92");
        LedgerDTO existLedger = ledgerService.get(record.getLedgerId());
        LedgerCategoryDTO existCategory = ledgerCategoryService.get(record.getCategoryId());
        if (systemUserService.get(userId) == null || existLedger == null || existCategory == null) {
            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不存在");
        }

//       账本大类属于该账本
        if (!existCategory.getLedgerId().equals(existLedger.getId())) {
            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不存在此类");
        }
        record.setAmount(record.getAmount() * 10000);
//        个人普通账本/合作普通账本：如果账本的拥有者不为当前用户
        if (existLedger.getType().equals(LedgerTypeEnum.COMMON)) {
            if (!existLedger.getOwnerId().equals(userId)) {
                throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操作");
            }
            return super.save(record);
        } else if (existLedger.getType().equals(LedgerTypeEnum.CO_COMMON)) {
            if (!coLedgerUserService.getLedgerPermission(existLedger.getId(), userId, true)) {
                throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操作");
            }
            return super.save(record);
        }
        return null;
    }

    @Override
    public LedgerEntryDTO update(LedgerEntryDTO record) throws ServiceException {
//        UUID userId = UUID.fromString("a087c0e3-2577-4a17-b435-7b12f7aa51e0");
        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
//        UUID userId = UUID.fromString("a7f39ae9-8a75-4914-8737-3f6a979ebb92");
        LedgerEntryDTO exist = get(record.getId());
//       判断账目存在
        LedgerDTO existLedger = ledgerService.get(record.getLedgerId());
        LedgerCategoryDTO existCategory = ledgerCategoryService.get(record.getCategoryId());
//        账户不存在， 当前用户不存在 大类不存在，大类不属于该账本
        if (exist == null || systemUserService.get(userId) == null || existCategory == null) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
        }

        if (!existCategory.getLedgerId().equals(existLedger.getId())) {
            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不存在此类");
        }

        if (!exist.getLedgerId().equals(record.getLedgerId())) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "属于账本不可变");
        }
        record.setAmount(record.getAmount() * 10000);
        if (existLedger.getType().equals(LedgerTypeEnum.COMMON)) {
            if (!existLedger.getOwnerId().equals(userId)) {
                throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操作");
            }
            return super.update(record);
        } else if (existLedger.getType().equals(LedgerTypeEnum.CO_COMMON)) {
            if (!coLedgerUserService.getLedgerPermission(existLedger.getId(), userId, true)) {
                throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操作");
            }
            return super.update(record);
        }
        return null;
    }

//    @Override
//    public void remove(UUID uuid) throws ServiceException {
//        //        判断当前userId存在
////        if (systemUserService.get(userId) == null) {
////            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "用户不存在");
////        }
////        LedgerEntryDTO exist = get(uuid);
////        if (exist == null) {
////            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "没有该账目");
////        }
//
////       判断账本存在
////        LedgerDTO existLedger = ledgerService.get(exist.getLedgerId());
//
////        单人账本/理财账本：如果账本的拥有者不为当前用户
////        if (((existLedger.getType() & 2) == 2 || (existLedger.getType() & 1) == 1) && existLedger.getOwnerId() != userId) {
////            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
////        }
//
////        多人账本： 拥有者不为当前用户 或者当前用户的 is writable = 0
////        if ((existLedger.getType() & 8) == 8) {
////           查询co_ledger_user   ledger_id+userId+is_writable=1; 如果查不到报错
////            if (coLedgerUserService.getLedgerPermission(ledger_id, userId, isWritable) == null) {
////                throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
////            }
////        }
//        super.remove(uuid);
//    }


    @Override
    protected LedgerEntryDTO doToDto(LedgerEntryDO record) {
        if (record == null) {
            return null;
        }
        LedgerEntryDTO result = buildDTOInstance();
        BeanUtils.copyProperties(record, result);
        result.setId(UUID.fromString(record.getUuid()));
        result.setCategoryId(UUID.fromString(record.getCategoryId()));
        result.setLedgerId(UUID.fromString(record.getLedgerId()));
        return result;
    }

    @Override
    protected LedgerEntryDO dtoToDo(LedgerEntryDTO record) {
        if (record == null) {
            return null;
        }
        LedgerEntryDO result = buildDOInstance();
        BeanUtils.copyProperties(record, result);
        if (record.getId() != null) {
            result.setUuid(record.getId().toString());
        }
        result.setLedgerId(record.getLedgerId().toString());
        result.setCategoryId(record.getCategoryId().toString());
        return result;
    }
}