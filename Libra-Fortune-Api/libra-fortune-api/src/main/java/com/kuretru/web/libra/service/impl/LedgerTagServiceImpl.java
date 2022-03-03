package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.api.common.constant.code.ServiceErrorCodes;
import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerTagDO;
import com.kuretru.web.libra.entity.query.LedgerTagQuery;
import com.kuretru.web.libra.entity.transfer.LedgerTagDTO;
import com.kuretru.web.libra.entity.transfer.SysUserDTO;
import com.kuretru.web.libra.mapper.LedgerTagMapper;
import com.kuretru.web.libra.service.LedgerTagService;
import com.kuretru.web.libra.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
        if (userService.get(record.getUserId()) == null) {
            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "该用户不存在");
        }
        QueryWrapper<LedgerTagDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", record.getName());
        queryWrapper.eq("user_id", record.getUserId().toString());
        if (mapper.selectOne(queryWrapper) != null) {
            throw new ServiceException.BadRequest(UserErrorCodes.USER_REPEATED_REQUEST, "该用户已存在该tag标签");
        }
        return super.save(record);
    }

    @Override
    public LedgerTagDTO update(LedgerTagDTO record) throws ServiceException {
//        if(!record.getUserId().toString().equals(已登录userid)){
//            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操作其他账户");
//        }

//        判断userId 是否存在（判断了上面的似乎没必要再判断了 不过先写着）
        SysUserDTO userDTO = userService.get(record.getUserId());
        if (null == userDTO) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "该用户不存在");
        }

//        判断是否重复
        LedgerTagDO data = dtoToDo(record);
        data.setUpdateTime(Instant.now());
        QueryWrapper<LedgerTagDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", record.getName());
        queryWrapper.eq("user_id", data.getUserId());
        if (null != mapper.selectOne(queryWrapper)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "已有相同tag");
        }
        queryWrapper = new QueryWrapper<>();
//        判断tag是否属于该用户
        queryWrapper.eq("uuid", data.getUuid());
        queryWrapper.eq("user_id", data.getUserId());
        int rows = mapper.update(data, queryWrapper);
        if (0 == rows) {
            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "指定资源不存在");
        } else if (1 != rows) {
            throw new ServiceException.InternalServerError(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "发现多个相同业务主键");
        }
        return get(record.getId());
    }


    @Override
    protected LedgerTagDTO doToDto(LedgerTagDO record) {
        LedgerTagDTO result = super.doToDto(record);
        if (result != null) {
            result.setUserId(UUID.fromString(record.getUserId()));
        }
        return result;
    }

    @Override
    protected LedgerTagDO dtoToDo(LedgerTagDTO record) {
        LedgerTagDO result = super.dtoToDo(record);
        if (result != null) {
            result.setUserId(record.getUserId().toString());
        }
        return result;
    }

}
