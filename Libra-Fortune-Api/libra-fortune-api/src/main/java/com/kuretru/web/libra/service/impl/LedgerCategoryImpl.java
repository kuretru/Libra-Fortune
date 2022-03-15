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
import com.kuretru.web.libra.service.LedgerService;
import com.kuretru.web.libra.service.SystemUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LedgerCategoryImpl extends BaseServiceImpl<LedgerCategoryMapper, LedgerCategoryDO, LedgerCategoryDTO, LedgerCategoryQuery> implements LedgerCategoryService {
    private final SystemUserService userService;
    private final LedgerService ledgerService;
    private final CoLedgerUserService coLedgerUserService;

    @Autowired
    public LedgerCategoryImpl(LedgerCategoryMapper mapper, SystemUserService userService, LedgerService ledgerService, CoLedgerUserService coLedgerUserService) {
        super(mapper, LedgerCategoryDO.class, LedgerCategoryDTO.class, LedgerCategoryQuery.class);
        this.userService = userService;
        this.ledgerService = ledgerService;
        this.coLedgerUserService = coLedgerUserService;
    }

    @Override
    public synchronized LedgerCategoryDTO save(LedgerCategoryDTO record) throws ServiceException {
        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
        LedgerDTO existLedger = ledgerService.get(record.getLedgerId());

//        账本不存在或当前用户不存在
        if (existLedger == null || userService.get(userId) == null) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账本不存在");
        }
//        不可重复添加
        QueryWrapper<LedgerCategoryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ledger_id", record.getLedgerId().toString());
        queryWrapper.eq("name", record.getName());
        if (mapper.exists(queryWrapper)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可重复添加");
        }
        if (existLedger.getType().equals(LedgerTypeEnum.COMMON) || existLedger.getType().equals(LedgerTypeEnum.FINANCIAL)) {
//        单人普通/理财账本：如果账本的拥有者不为当前用户
            if (!existLedger.getOwnerId().equals(userId)) {
                throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
            }
        } else {
//            如果是合作普通/理财账本 用coLedgerUserService
//            查询co_ledger_user ledger_id +userId + is_writable = 1;
//            如果查不到报错
            if (!coLedgerUserService.getLedgerPermission(existLedger.getId(), userId, true)) {
                throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
            }
        }
        return super.save(record);
    }

    @Override
    public LedgerCategoryDTO update(LedgerCategoryDTO record) throws ServiceException {
        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
        LedgerDTO existLedger = ledgerService.get(record.getLedgerId());
//        账本不存在或当前用户不存在
        if (existLedger == null || userService.get(userId) == null) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账本不存在");
        }

        if (existLedger.getType().equals(LedgerTypeEnum.COMMON) || existLedger.getType().equals(LedgerTypeEnum.FINANCIAL)) {
//        单人普通/理财账本：如果账本的拥有者不为当前用户
            if (!existLedger.getOwnerId().equals(userId)) {
                throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
            }
        } else {
//            如果是合作普通/理财账本 用coLedgerUserService
//            查询co_ledger_user ledger_id +userId + is_writable = 1;
//            如果查不到报错
            if (!coLedgerUserService.getLedgerPermission(existLedger.getId(), userId, true)) {
                throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
            }
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
//    @Override
//    public void remove(UUID uuid) throws ServiceException {
//        判断当前userId存在
//        if (systemUserService.get(userId) == null) {
//            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "用户不存在");
//        }

//       判断账本大类存在
//        LedgerCategoryDTO ledgerCategory = get(uuid);
//        if (ledgerCategory == null) {
//            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "大类不存在");
//        }

//       判断账本存在
//        LedgerDTO existLedger = ledgerService.get(ledgerCategory.getLedgerId());
//        if (existLedger == null) {
//            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账本不存在");
//        }

//        单人账本/理财账本：如果账本的拥有者不为当前用户
//        if (((existLedger.getType() & 2) == 2 || (existLedger.getType() & 1) == 1) && existLedger.getOwnerId() != userId) {
//            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
//        }

//        多人账本：拥有者不为当前用户 或者当前用户的 is writable = 0
//        if ((existLedger.getType() & 8) == 8) {
//            查询co_ledger_user ledger_id +userId + is_writable = 1;
//            如果查不到报错
//            if (coLedgerUserService.getLedgerPermission(existLedger.getId(), userId, isWritable) == null) {
//                throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
//            }
//        }
//        super.remove(uuid);
//    }

    @Override
    protected LedgerCategoryDTO doToDto(LedgerCategoryDO record) {
        if (record == null) {
            return null;
        }
        LedgerCategoryDTO result = buildDTOInstance();
        BeanUtils.copyProperties(record, result);
        result.setId(UUID.fromString(record.getUuid()));
        result.setLedgerId(UUID.fromString(record.getLedgerId()));
        return result;
    }

    @Override
    protected LedgerCategoryDO dtoToDo(LedgerCategoryDTO record) {
        if (record == null) {
            return null;
        }
        LedgerCategoryDO result = buildDOInstance();
        BeanUtils.copyProperties(record, result);
        if (record.getId() != null) {
            result.setUuid(record.getId().toString());
        }
        result.setLedgerId(record.getLedgerId().toString());
        return result;
    }
}