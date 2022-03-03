package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.api.common.constant.code.ServiceErrorCodes;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.manager.PasswordSaltManager;
import com.kuretru.api.common.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.SysUserDO;
import com.kuretru.web.libra.entity.query.SysUserQuery;
import com.kuretru.web.libra.entity.transfer.SysUserDTO;
import com.kuretru.web.libra.mapper.SysUserMapper;
import com.kuretru.web.libra.service.SysUserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUserDO, SysUserDTO, SysUserQuery> implements SysUserService {
    PasswordSaltManager passwordSaltManager;

    @Autowired
    public SysUserServiceImpl(SysUserMapper mapper, PasswordSaltManager passwordSaltManager) {
        super(mapper, SysUserDO.class, SysUserDTO.class, SysUserQuery.class);
        this.passwordSaltManager = passwordSaltManager;
    }

    @Override
    public SysUserDTO get(String username, String password) throws ServiceException.InternalServerError {
        QueryWrapper<SysUserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        SysUserDO record = mapper.selectOne(queryWrapper);
        if (record == null) {
            throw new ServiceException.InternalServerError(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "用户名或密码错误");
        }
        String salt = record.getSalt();
        String passwordWithSalt = (salt + password + salt.substring(4));
        String md5_password = DigestUtils.md5DigestAsHex(passwordWithSalt.getBytes());
        if (!md5_password.equals(record.getPassword())) {
            throw new ServiceException.InternalServerError(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "用户名或密码错误");
        }
        return super.get(record.getId());
    }

    @Override
    public SysUserDTO get(QueryWrapper<SysUserDO> userQueryWrapper) {
        return doToDto(mapper.selectOne(userQueryWrapper));
    }


    @Override
    public synchronized SysUserDTO save(SysUserDTO record) throws ServiceException {
        QueryWrapper<SysUserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", record.getUsername());
        if (mapper.selectOne(queryWrapper) == null) {
            throw new ServiceException.InternalServerError(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "用户名重复，请更改用户名后重新提交请求");
        }

        String salt = passwordSaltManager.generateSalt();
        String password = passwordSaltManager.mixSalt(record.getPassword(),salt);
        record.setPassword(password);
//        如果没有写昵称，默认名称和用户名一致
        if (record.getNickname() == null) {
            record.setNickname(record.getUsername());
        }
        return super.save(record);
    }


}
