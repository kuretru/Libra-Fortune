package com.kuretru.web.libra.service.impl;

import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerEntryDO;
import com.kuretru.web.libra.entity.query.LedgerEntryQuery;
import com.kuretru.web.libra.entity.transfer.LedgerCategoryDTO;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.entity.transfer.LedgerEntryDTO;
import com.kuretru.web.libra.mapper.LedgerEntryMapper;
import com.kuretru.web.libra.service.LedgerCategoryService;
import com.kuretru.web.libra.service.LedgerEntryService;
import com.kuretru.web.libra.service.LedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LedgerEntryImpl extends BaseServiceImpl<LedgerEntryMapper, LedgerEntryDO, LedgerEntryDTO, LedgerEntryQuery> implements LedgerEntryService {
    private final LedgerService ledgerService;
    private final LedgerCategoryService ledgerCategoryService;

    @Autowired
    public LedgerEntryImpl(LedgerEntryMapper mapper, LedgerService ledgerService, LedgerCategoryService ledgerCategoryService) {
        super(mapper, LedgerEntryDO.class, LedgerEntryDTO.class, LedgerEntryQuery.class);
        this.ledgerService = ledgerService;
        this.ledgerCategoryService = ledgerCategoryService;
    }

    @Override
    public synchronized LedgerEntryDTO save(LedgerEntryDTO record) throws ServiceException {
//        判断当前userId存在
//        if (systemUserService.get(userId) == null) {
//            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "用户不存在");
//        }

//       判断账本存在
        LedgerDTO existLedger = ledgerService.get(record.getLedgerId());
        if (existLedger == null) {
            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账本不存在");
        }

//       判断账本大类存在
        LedgerCategoryDTO existCategory = ledgerCategoryService.get(record.getCategoryId());
        if (existCategory == null) {
            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "大类不存在");
        }

//       账本大类存在并且属于该账本
        if (existCategory.getLedgerId().equals(existLedger.getId())) {
            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
        }

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
    public LedgerEntryDTO update(LedgerEntryDTO record) throws ServiceException {
        //        判断当前userId存在
//        if (systemUserService.get(userId) == null) {
//            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "用户不存在");
//        }

//       判断账本存在
        LedgerDTO existLedger = ledgerService.get(record.getLedgerId());
        if (existLedger == null) {
            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账本不存在");
        }

//       判断账本大类存在
        LedgerCategoryDTO existCategory = ledgerCategoryService.get(record.getCategoryId());
        if (existCategory == null) {
            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "大类不存在");
        }

//       账本大类存在并且属于该账本
        if (existCategory.getLedgerId().equals(existLedger.getId())) {
            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
        }

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

        return super.update(record);
    }

    @Override
    public void remove(UUID uuid) throws ServiceException {
        //        判断当前userId存在
//        if (systemUserService.get(userId) == null) {
//            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "用户不存在");
//        }
//        LedgerEntryDTO exist = get(uuid);
//        if (exist == null) {
//            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "没有该账目");
//        }

//       判断账本存在
//        LedgerDTO existLedger = ledgerService.get(exist.getLedgerId());

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
        super.remove(uuid);
    }
}