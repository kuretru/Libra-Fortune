package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.UserTagDO;
import com.kuretru.web.libra.entity.query.UserTagQuery;
import com.kuretru.web.libra.entity.transfer.SystemUserDTO;
import com.kuretru.web.libra.entity.transfer.UserTagDTO;
import com.kuretru.web.libra.mapper.UserTagMapper;
import com.kuretru.web.libra.service.SystemUserService;
import com.kuretru.web.libra.service.UserTagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserTagServiceImpl extends BaseServiceImpl<UserTagMapper, UserTagDO, UserTagDTO, UserTagQuery> implements UserTagService {
    private final SystemUserService systemUserService;


    @Autowired
    public UserTagServiceImpl(UserTagMapper mapper, SystemUserService systemUserService) {
        super(mapper, UserTagDO.class, UserTagDTO.class, UserTagQuery.class);
        this.systemUserService = systemUserService;

    }

    public synchronized UserTagDTO save(UserTagDTO record) throws ServiceException {
//        UUID userId = UUID.fromString("a087c0e3-2577-4a17-b435-7b12f7aa51e0");
        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
//        UUID userId = UUID.fromString("a7f39ae9-8a75-4914-8737-3f6a979ebb92");
        if (!userId.equals(record.getUserId())) {
            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操作");
        }
        SystemUserDTO existUser = systemUserService.get(record.getUserId());
        if (existUser == null) {
            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "用户不存在");
        }
        if (record.getName().equals("")) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "Tag不可为空");
        }
        QueryWrapper<UserTagDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", record.getName());
        if (mapper.exists(queryWrapper)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "该Tag已存在");
        }
        return super.save(record);
    }

    public UserTagDTO update(UserTagDTO record) throws ServiceException {
//        UUID userId = UUID.fromString("a087c0e3-2577-4a17-b435-7b12f7aa51e0");
        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
//        UUID userId = UUID.fromString("a7f39ae9-8a75-4914-8737-3f6a979ebb92");
        UserTagDTO oldUserTagDTO = get(record.getId());
        if (oldUserTagDTO == null) {
            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "找不到该Tag");

        }
        if (!userId.equals(oldUserTagDTO.getUserId())) {
            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操作");
        }

        if (!record.getUserId().equals(oldUserTagDTO.getUserId())) {
            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可更该用户");
        }

        SystemUserDTO existUser = systemUserService.get(record.getUserId());
        if (existUser == null) {
            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "用户不存在");
        }
        if (record.getName().equals("")) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "Tag不可为空");
        }
        QueryWrapper<UserTagDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", record.getName());
        if (mapper.exists(queryWrapper)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "该Tag已存在");
        }
        return super.update(record);
    }


    @Override
    protected UserTagDTO doToDto(UserTagDO record) {
        if (record == null) {
            return null;
        }
        UserTagDTO result = buildDTOInstance();
        BeanUtils.copyProperties(record, result);
        result.setId(UUID.fromString(record.getUuid()));
        result.setUserId(UUID.fromString(record.getUserId()));
        return result;
    }

    @Override
    protected UserTagDO dtoToDo(UserTagDTO record) {
        if (record == null) {
            return null;
        }
        UserTagDO result = buildDOInstance();
        BeanUtils.copyProperties(record, result);
        if (record.getId() != null) {
            result.setUuid(record.getId().toString());
        }
        result.setUserId(record.getUserId().toString());
        return result;
    }


    @Override
    public Boolean userExistTag(UUID userId, UUID tagId) {
        QueryWrapper<UserTagDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId.toString());
        queryWrapper.eq("uuid", tagId.toString());
        return mapper.exists(queryWrapper);
    }
}