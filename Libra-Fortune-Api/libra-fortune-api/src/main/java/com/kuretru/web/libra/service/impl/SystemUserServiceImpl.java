package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.manager.PasswordSaltManager;
import com.kuretru.api.common.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.SystemUserDO;
import com.kuretru.web.libra.entity.query.SystemUserQuery;
import com.kuretru.web.libra.entity.transfer.SystemUserDTO;
import com.kuretru.web.libra.mapper.SystemUserMapper;
import com.kuretru.web.libra.service.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SystemUserServiceImpl extends BaseServiceImpl<SystemUserMapper, SystemUserDO, SystemUserDTO, SystemUserQuery> implements SystemUserService {

    private final PasswordSaltManager passwordSaltManager;

    @Autowired
    public SystemUserServiceImpl(SystemUserMapper mapper, PasswordSaltManager passwordSaltManager) {
        super(mapper, SystemUserDO.class, SystemUserDTO.class, SystemUserQuery.class);
        this.passwordSaltManager = passwordSaltManager;
    }

    @Override
    public synchronized SystemUserDTO save(SystemUserDTO record) throws ServiceException {
        QueryWrapper<SystemUserDO> queryWrapper = new QueryWrapper<>();
        if (record.getUsername().equals("")) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "用户名不可为空");
        }
        queryWrapper.eq("username", record.getUsername());
        if (mapper.exists(queryWrapper)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "该用户名已存在");
        }
        String salt = passwordSaltManager.generateSalt();
        String password = passwordSaltManager.mixSalt(record.getPassword(), salt);
        record.setSalt(salt);
        record.setPassword(password);
//        如果没有写昵称，默认名称和用户名一致
        if (record.getNickname().equals("")) {
            record.setNickname(record.getUsername());
        }
        return super.save(record);
    }

    @Override
    public SystemUserDTO get(UUID uuid) {
        //        如果操作的不是当前账户
        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
        if (!userId.equals(uuid)) {
//            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "用户不存在");
        }
        return super.get(uuid);
    }

    @Override
    public SystemUserDTO update(SystemUserDTO record) throws ServiceException {
//        判断传过来的user是否存在
        SystemUserDTO user = get(record.getId());
//        如果操作的不是当前账户  或  传过来的user不存在
//        if (userId != record.getId() || user == null) {
//            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "用户不存在");
//        }
        if (!user.getUsername().equals(record.getUsername())) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "用户名不可更改");
        }
        return super.update(record);
    }

    @Override
    public void remove(UUID uuid) throws ServiceException {
//        如果操作的不是当前账户
        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a338e68");
        if (userId != uuid) {
            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "用户不存在");
        }
        super.remove(uuid);
    }
}