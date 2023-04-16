package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.authentication.context.AccessTokenContext;
import com.kuretru.microservices.web.constant.code.ServiceErrorCodes;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerTagDO;
import com.kuretru.web.libra.entity.query.LedgerTagQuery;
import com.kuretru.web.libra.entity.transfer.LedgerTagDTO;
import com.kuretru.web.libra.mapper.LedgerTagMapper;
import com.kuretru.web.libra.service.LedgerTagService;
import org.mapstruct.Mapper;
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
    public LedgerTagDTO update(LedgerTagDTO record) throws ServiceException {
        // 不能把别人的记录修改为自己的
        LedgerTagDTO old = get(record.getId());
        if (old == null) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "指定记录不存在");
        }
        verifyDTO(old);

        return super.update(record);
    }

    @Override
    public void remove(UUID uuid) throws ServiceException {
        throw new ServiceException(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "尚未实现");
    }

    @Override
    protected void verifyDTO(LedgerTagDTO record) throws ServiceException {
        if (!record.getUserId().equals(AccessTokenContext.getUserId())) {
            throw new ServiceException(UserErrorCodes.ACCESS_UNAUTHORIZED, "只能修改自己的账本标签");
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

    @Mapper(componentModel = "spring")
    interface LedgerTagEntityMapper extends BaseServiceImpl.BaseEntityMapper<LedgerTagDO, LedgerTagDTO> {

    }

}
