package com.kuretru.web.libra.service.impl;

import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerDO;
import com.kuretru.web.libra.entity.query.LedgerQuery;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.mapper.LedgerMapper;
import com.kuretru.web.libra.service.LedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LedgerServiceImpl extends BaseServiceImpl<LedgerMapper, LedgerDO, LedgerDTO, LedgerQuery> implements LedgerService {


    @Autowired
    public LedgerServiceImpl(LedgerMapper mapper) {
        super(mapper, LedgerDO.class, LedgerDTO.class, LedgerQuery.class);
    }

    @Override
    public synchronized LedgerDTO save(LedgerDTO record) throws ServiceException {
//        判断当前userId存在
//        SystemUserDTO existUser = get(userId);
//        if (existUser == null) {
//            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "用户不存在");
//        }
        if(!((record.getType()&8)==8 || (record.getType()&1)==1 ||(record.getType()&2)==2)){
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "请输入正确的类型");
        }
//        记录当前userId
//        record.setOwnerId(userId);
        return super.save(record);
    }

    @Override
    public LedgerDTO update(LedgerDTO record) throws ServiceException {
//        判断当前userId存在
//        SystemUserDTO existUser = get(userId)
//        if (existUser == null) {
//            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "用户不存在");
//        }

//       判断账本存在
//        LedgerDTO exist = get(record.getId());
//        if (exist == null) {
//            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账本不存在");
//        }

//        单人账本/理财账本：如果账本的拥有者不为当前用户
//        if (((exist.getType() & 2) == 2 || (exist.getType() & 1) == 1) && exist.getOwnerId() != userId) {
//            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
//        }

//        多人账本：拥有者不为当前用户 或者当前用户的 is writable = 0
//        if ((exist.getType() & 8) == 8) {
//            查询co_ledger_user ledger_id +userId + is_writable = 1;
//            如果查不到报错
//            if (coLedgerUserService.getLedgerPermission(ledger_id, userId, isWritable) == null) {
//                throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
//            }
//        }

        return super.update(record);
    }

    @Override
    public void remove(UUID uuid) throws ServiceException {
        //        判断当前userId存在
//        SystemUserDTO existUser = get(userId)
//        if (existUser == null) {
//            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "用户不存在");
//        }

//       判断账本存在
//        LedgerDTO exist = get(uuid);
//        if (exist == null) {
//            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账本不存在");
//        }

//        单人账本/理财账本：如果账本的拥有者不为当前用户
//        if (((exist.getType() & 2) == 2 || (exist.getType() & 1) == 1) && exist.getOwnerId() != userId) {
//            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
//        }

//        多人账本：拥有者不为当前用户 或者当前用户的 is writable = 0
//        if ((exist.getType() & 8) == 8) {
//            查询co_ledger_user ledger_id +userId + is_writable = 1;
//            如果查不到报错
//            if (coLedgerUserService.getLedgerPermission(ledger_id, userId, isWritable) == null) {
//                throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
//            }
//        }
        super.remove(uuid);
    }
}