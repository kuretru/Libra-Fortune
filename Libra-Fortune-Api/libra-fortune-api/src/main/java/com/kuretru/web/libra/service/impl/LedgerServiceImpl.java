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
//        记录当前userId
//        record.setOwnerId(userId);
        return super.save(record);
    }

    @Override
    protected void verifyDTO(LedgerDTO record) throws ServiceException {
//        判断当前userId存在
//        SystemUserDTO existUser = get(userId)
//        if (existUser == null) {
//            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "用户不存在");
//        }

//       判断账本存在
        LedgerDTO exist = get(record.getId());
        if (exist == null) {
            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账本不存在");
        }

//        如果账本的拥有者不为当前用户
//        if (exist.getOwnerId()!=userId) {
//            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
//        }
    }

}