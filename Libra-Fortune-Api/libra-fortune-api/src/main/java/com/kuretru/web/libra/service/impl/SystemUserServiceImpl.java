package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.api.common.constant.code.ServiceErrorCodes;
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

import java.time.Instant;

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
        if (mapper.selectOne(queryWrapper) != null) {
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
    public SystemUserDTO update(SystemUserDTO record) throws ServiceException {
        SystemUserDTO user = get(record.getId());
        if (user == null) {
            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "该用户不存在");
        }
        if (record.getUsername().equals("")) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "用户名不可为空");
        }
        if (!user.getUsername().equals(record.getUsername())) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "用户名不可更改");
        }
        if (!record.getPassword().equals("")) {
            String salt = passwordSaltManager.generateSalt();
            String password = passwordSaltManager.mixSalt(record.getPassword(), salt);
            user.setSalt(salt);
            user.setPassword(password);
        }
        SystemUserDO recordDo = dtoToDo(record);
        recordDo.setUpdateTime(Instant.now());
        QueryWrapper<SystemUserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", recordDo.getUuid());
        int rows = mapper.update(recordDo, queryWrapper);
        if (0 == rows) {
        } else if (1 != rows) {
            throw new ServiceException.InternalServerError(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "发现多个相同业务主键");
        }
        return get(record.getId());
    }
}