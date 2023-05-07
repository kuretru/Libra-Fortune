package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.authentication.context.AccessTokenContext;
import com.kuretru.microservices.web.constant.code.ServiceErrorCodes;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerTagDO;
import com.kuretru.web.libra.entity.mapper.LedgerTagEntityMapper;
import com.kuretru.web.libra.entity.query.LedgerTagQuery;
import com.kuretru.web.libra.entity.transfer.LedgerTagDTO;
import com.kuretru.web.libra.mapper.LedgerTagMapper;
import com.kuretru.web.libra.service.LedgerTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Service
public class LedgerTagServiceImpl
        extends BaseServiceImpl<LedgerTagMapper, LedgerTagDO, LedgerTagDTO, LedgerTagQuery>
        implements LedgerTagService {

    @Autowired
    public LedgerTagServiceImpl(LedgerTagMapper mapper, LedgerTagEntityMapper entityMapper) {
        super(mapper, entityMapper);
    }

    @Override
    public LedgerTagDTO save(LedgerTagDTO record) throws ServiceException {
        record.setUserId(AccessTokenContext.getUserId());
        return super.save(record);
    }

    @Override
    public LedgerTagDTO update(LedgerTagDTO record) throws ServiceException {
        record.setUserId(AccessTokenContext.getUserId());
        LedgerTagDTO old = get(record.getId());
        if (old == null) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "指定记录不存在");
        } else if (!old.getUserId().equals(record.getUserId())) {
            // 不能把别人的记录修改为自己的
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "无法修改非自己的账本标签");
        }

        return super.update(record);
    }

    @Override
    public void remove(UUID uuid) throws ServiceException {
        throw new ServiceException(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "尚未实现");
    }

    @Override
    protected QueryWrapper<LedgerTagDO> buildQueryWrapper(LedgerTagQuery query) {
        QueryWrapper<LedgerTagDO> queryWrapper = super.buildQueryWrapper(query);
        queryWrapper.eq("user_id", AccessTokenContext.getUserId().toString());
        return queryWrapper;
    }

    @Override
    protected void verifyDO(LedgerTagDO record) throws ServiceException {
        if (!UUID.fromString(record.getUserId()).equals(AccessTokenContext.getUserId())) {
            throw new ServiceException(UserErrorCodes.ACCESS_UNAUTHORIZED, "只能查询自己的账本标签");
        }
    }

    @Override
    protected LedgerTagDTO findUniqueRecord(LedgerTagDTO record) {
        QueryWrapper<LedgerTagDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", record.getUserId());
        queryWrapper.eq("name", record.getName());
        LedgerTagDO result = mapper.selectOne(queryWrapper);
        return entityMapper.doToDto(result);
    }

}
