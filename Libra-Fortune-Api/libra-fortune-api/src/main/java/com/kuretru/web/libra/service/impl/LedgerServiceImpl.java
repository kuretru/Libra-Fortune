package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.api.common.constant.code.ServiceErrorCodes;
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

import java.time.Instant;

@Service
public class LedgerServiceImpl extends BaseServiceImpl<LedgerMapper, LedgerDO, LedgerDTO, LedgerQuery> implements LedgerService {


    @Autowired
    public LedgerServiceImpl(LedgerMapper mapper) {
        super(mapper, LedgerDO.class, LedgerDTO.class, LedgerQuery.class);
    }

    // save要获取用户id 暂时不写了Orz


    //update 改名字 之前也要确认 账本 是否归属于这个用户
    public LedgerDTO update(LedgerDTO record) throws ServiceException {
//        账本 是否归属于这个用户 需要用户id
//        QueryWrapper<LedgerDO> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("uuid", record.getId());
//        if(null == mapper.selectByUserAndLedger(userId,record.getId().toString())){
//            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可修改非自己的账本");
//        };
        LedgerDO data = dtoToDo(record);
        data.setUpdateTime(Instant.now());
        QueryWrapper<LedgerDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", data.getUuid());
        int rows = mapper.update(data, queryWrapper);
        if (0 == rows) {
            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "指定资源不存在");
        } else if (1 != rows) {
            throw new ServiceException.InternalServerError(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "发现多个相同业务主键");
        }
        return get(record.getId());
    }
}
