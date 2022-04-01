package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerEntryDO;
import com.kuretru.web.libra.entity.enums.LedgerTypeEnum;
import com.kuretru.web.libra.entity.query.LedgerEntryQuery;
import com.kuretru.web.libra.entity.transfer.LedgerCategoryDTO;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.entity.transfer.LedgerEntryDTO;
import com.kuretru.web.libra.mapper.LedgerEntryMapper;
import com.kuretru.web.libra.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LedgerEntryServiceImpl extends BaseServiceImpl<LedgerEntryMapper, LedgerEntryDO, LedgerEntryDTO, LedgerEntryQuery> implements LedgerEntryService {

    private final LedgerService ledgerService;
    private final LedgerCategoryService ledgerCategoryService;
    private final CoLedgerUserService coLedgerUserService;
    private final CoLedgerEntryService coLedgerEntryService;

    @Autowired
    public LedgerEntryServiceImpl(LedgerEntryMapper mapper, LedgerService ledgerService, LedgerCategoryService ledgerCategoryService, CoLedgerUserService coLedgerUserService, @Lazy CoLedgerEntryService coLedgerEntryService) {
        super(mapper, LedgerEntryDO.class, LedgerEntryDTO.class, LedgerEntryQuery.class);
        this.ledgerService = ledgerService;
        this.ledgerCategoryService = ledgerCategoryService;
        this.coLedgerUserService = coLedgerUserService;
        this.coLedgerEntryService = coLedgerEntryService;
    }

    @Override
    public synchronized LedgerEntryDTO save(LedgerEntryDTO record) throws ServiceException {
        UUID userId = UUID.fromString("a087c0e3-2577-4a17-b435-7b12f7aa51e0");
        LedgerDTO existLedger = ledgerService.get(record.getLedgerId());
        LedgerCategoryDTO existCategory = ledgerCategoryService.get(record.getCategoryId());
        if (existLedger == null || existCategory == null) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账本/大类不存在");
        }

//       账本大类属于该账本
        if (!existCategory.getLedgerId().equals(existLedger.getId())) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "该账本不存在此类");
        }

//        个人普通账本/合作普通账本：如果账本的拥有者不为当前用户
        if (existLedger.getType().equals(LedgerTypeEnum.COMMON)) {
            if (!existLedger.getOwnerId().equals(userId)) {
                throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操作");
            }
        } else if (existLedger.getType().equals(LedgerTypeEnum.CO_COMMON)) {
            if (!coLedgerUserService.getLedgerPermission(existLedger.getId(), userId, true)) {
                throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操作");
            }
        } else {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账本类型错误");
        }
        return super.save(record);
    }

    @Override
    public LedgerEntryDTO update(LedgerEntryDTO record) throws ServiceException {
//        UUID userId = UUID.fromString("a087c0e3-2577-4a17-b435-7b12f7aa51e0");
        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
//        UUID userId = UUID.fromString("a7f39ae9-8a75-4914-8737-3f6a979ebb92");
        LedgerEntryDTO oldRecord = get(record.getId());
        LedgerCategoryDTO newCategory = ledgerCategoryService.get(record.getCategoryId());
        if (oldRecord == null || newCategory == null) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账目/大类不存在");
        }

        if (!oldRecord.getLedgerId().equals(record.getLedgerId())) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "属于账本不可变");
        }

//        大类不属于该账本
        LedgerDTO existLedger = ledgerService.get(record.getLedgerId());
        if (!newCategory.getLedgerId().equals(existLedger.getId())) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "该账本不存在此类");
        }

        if (existLedger.getType().equals(LedgerTypeEnum.COMMON)) {
            if (!existLedger.getOwnerId().equals(userId)) {
                throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操作");
            }
        } else if (existLedger.getType().equals(LedgerTypeEnum.CO_COMMON)) {
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
        //        UUID userId = UUID.fromString("a087c0e3-2577-4a17-b435-7b12f7aa51e0");
        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
//        UUID userId = UUID.fromString("a7f39ae9-8a75-4914-8737-3f6a979ebb92");
        LedgerEntryDTO oldLedgerEntry = get(uuid);
        if (oldLedgerEntry == null) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账目不存在");
        }

//       判断账本存在
        LedgerDTO existLedger = ledgerService.get(oldLedgerEntry.getLedgerId());
//        个人普通账本/合作普通账本：如果账本的拥有者不为当前用户
        if (existLedger.getType().equals(LedgerTypeEnum.COMMON)) {
            if (!existLedger.getOwnerId().equals(userId)) {
                throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操作");
            }
        } else if (existLedger.getType().equals(LedgerTypeEnum.CO_COMMON)) {
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
    public Boolean existCategoryId(UUID categoryId) {
        QueryWrapper<LedgerEntryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId.toString());
        return mapper.exists(queryWrapper);
    }

    @Override
    protected LedgerEntryDTO doToDto(LedgerEntryDO record) {
        LedgerEntryDTO result = super.doToDto(record);
        if (record != null) {
            result.setCategoryId(UUID.fromString(record.getCategoryId()));
            result.setLedgerId(UUID.fromString(record.getLedgerId()));
        }
        return result;
    }

    @Override
    protected LedgerEntryDO dtoToDo(LedgerEntryDTO record) {
        LedgerEntryDO result = super.dtoToDo(record);
        if (result != null) {
            result.setLedgerId(record.getLedgerId().toString());
            result.setCategoryId(record.getCategoryId().toString());
        }
        return result;
    }


}
