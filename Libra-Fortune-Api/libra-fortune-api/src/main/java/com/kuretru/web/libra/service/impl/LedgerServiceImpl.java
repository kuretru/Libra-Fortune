package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.api.common.constant.code.ServiceErrorCodes;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerDO;
import com.kuretru.web.libra.entity.query.LedgerQuery;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.mapper.LedgerMapper;
import com.kuretru.web.libra.service.LedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class LedgerServiceImpl extends BaseServiceImpl<LedgerMapper, LedgerDO, LedgerDTO, LedgerQuery> implements LedgerService {


    @Autowired
    public LedgerServiceImpl(LedgerMapper mapper) {
        super(mapper, LedgerDO.class, LedgerDTO.class, LedgerQuery.class);
    }

    @Override
    public synchronized LedgerDTO save(LedgerDTO record) throws ServiceException {
//        获取userId
//        record.setId(userId)
        return super.save(record);
    }


    @Override
    public LedgerDTO update(LedgerDTO record) throws ServiceException {
        LedgerDTO exist = get(record.getId());
//        if (exist == null) {
//            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账本不存在");
//        }
//        if (exist.getOwnerId()!=userId) {
//            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做非自己账本");
//        }
//        LedgerDO recordDo = dtoToDo(record);
//        recordDo.setUpdateTime(Instant.now());
//        QueryWrapper<LedgerDO> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("uuid", recordDo.getId());
//        int rows = mapper.update(recordDo, queryWrapper);
//        if (0 == rows) {
//        } else if (1 != rows) {
//            throw new ServiceException.InternalServerError(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "发现多个相同业务主键");
//        }
        return get(record.getId());
    }
}