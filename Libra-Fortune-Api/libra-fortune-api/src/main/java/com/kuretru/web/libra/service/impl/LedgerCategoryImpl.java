package com.kuretru.web.libra.service.impl;

import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerCategoryDO;
import com.kuretru.web.libra.entity.query.LedgerCategoryQuery;
import com.kuretru.web.libra.entity.transfer.LedgerCategoryDTO;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.mapper.LedgerCategoryMapper;
import com.kuretru.web.libra.service.LedgerCategoryService;
import com.kuretru.web.libra.service.LedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LedgerCategoryImpl extends BaseServiceImpl<LedgerCategoryMapper, LedgerCategoryDO, LedgerCategoryDTO, LedgerCategoryQuery> implements LedgerCategoryService {

    public final LedgerService ledgerService;

    @Autowired
    public LedgerCategoryImpl(LedgerCategoryMapper mapper, LedgerService ledgerService) {
        super(mapper, LedgerCategoryDO.class, LedgerCategoryDTO.class, LedgerCategoryQuery.class);
        this.ledgerService = ledgerService;
    }

    @Override
    public synchronized LedgerCategoryDTO save(LedgerCategoryDTO record) throws ServiceException {
//                判断当前userId存在
//        if (systemUserService.get(userId) == null) {
//            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "用户不存在");
//        }

//       判断账本存在
//        LedgerDTO existLedger = ledgerService.get(record.getLedgerId());
//        if (existLedger == null) {
//            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账本不存在");
//        }

//        单人账本/理财账本：如果账本的拥有者不为当前用户
//        if (((existLedger.getType() & 2) == 2 || (existLedger.getType() & 1) == 1) && existLedger.getOwnerId() != userId) {
//            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
//        }

//        多人账本： 拥有者不为当前用户 或者当前用户的 is writable = 0
//        if ((existLedger.getType() & 8) == 8) {
//           查询co_ledger_user   ledger_id+userId+is_writable=1; 如果查不到报错
//            if (coLedgerUserService.getLedgerPermission(ledger_id, userId, isWritable) == null) {
//                throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
//            }
//        }
        return super.save(record);
    }


    @Override
    public LedgerCategoryDTO update(LedgerCategoryDTO record) throws ServiceException {
//        判断当前userId存在
//        if (systemUserService.get(userId) == null) {
//            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "用户不存在");
//        }

//       判断账本大类存在
//        if (get(record.getLedgerId()) == null) {
//            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "大类不存在");
//        }

//       判断账本存在
//        LedgerDTO existLedger = ledgerService.get(record.getLedgerId());
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
        return super.update(record);
    }

    @Override
    public void remove(UUID uuid) throws ServiceException {
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
        super.remove(uuid);
    }
}