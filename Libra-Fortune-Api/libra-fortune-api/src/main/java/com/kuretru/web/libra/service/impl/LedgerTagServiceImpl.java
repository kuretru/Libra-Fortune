package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.api.common.constant.code.ServiceErrorCodes;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerTagDO;
import com.kuretru.web.libra.entity.query.LedgerTagQuery;
import com.kuretru.web.libra.entity.transfer.LedgerTagDTO;
import com.kuretru.web.libra.mapper.LedgerTagMapper;
import com.kuretru.web.libra.service.LedgerTagService;
import com.kuretru.web.libra.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LedgerTagServiceImpl extends BaseServiceImpl<LedgerTagMapper, LedgerTagDO, LedgerTagDTO, LedgerTagQuery> implements LedgerTagService {
    private final SysUserService userService;

    @Autowired
    public LedgerTagServiceImpl(LedgerTagMapper mapper, SysUserService userService) {
        super(mapper, LedgerTagDO.class, LedgerTagDTO.class, LedgerTagQuery.class);
        this.userService = userService;
    }

    @Override
    public synchronized LedgerTagDTO save(LedgerTagDTO record) throws ServiceException {
        //        查询userId是不是已经有该tag
        if (userService.get(UUID.fromString(record.getUserId())) == null) {
            throw new ServiceException.InternalServerError(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "该用户不存在，不可操作");
        }
        QueryWrapper<LedgerTagDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", record.getName());
        queryWrapper.eq("user_id", record.getUserId());
        if (mapper.selectOne(queryWrapper) != null) {
            throw new ServiceException.InternalServerError(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "该用户已存在该tag标签，不可重复添加");
        }
        return super.save(record);
    }



}
