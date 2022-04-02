package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.authentication.entity.AccessTokenDTO;
import com.kuretru.microservices.authentication.entity.UserLoginDTO;
import com.kuretru.microservices.authentication.manager.AccessTokenManager;
import com.kuretru.microservices.oauth2.common.entity.GalaxyUserDTO;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.UserDO;
import com.kuretru.web.libra.entity.query.UserQuery;
import com.kuretru.web.libra.entity.transfer.UserDTO;
import com.kuretru.web.libra.mapper.UserMapper;
import com.kuretru.web.libra.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, UserDO, UserDTO, UserQuery> implements UserService {

    private final AccessTokenManager accessTokenManager;

    @Autowired
    public UserServiceImpl(UserMapper mapper, AccessTokenManager accessTokenManager) {
        super(mapper, UserDO.class, UserDTO.class);
        this.accessTokenManager = accessTokenManager;
    }

    private UserDO getByGeminiId(UUID geminiId) {
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("gemini_id", geminiId.toString());
        return mapper.selectOne(queryWrapper);
    }

    @Override
    public UserLoginDTO login(GalaxyUserDTO record) throws ServiceException {
        UserDO userDo = getByGeminiId(record.getId());
        if (userDo == null) {
            throw new ServiceException(UserErrorCodes.WRONG_USERNAME, "该用户不存在");
        }

        userDo.setNickname(record.getNickname());
        userDo.setAvatar(record.getAvatar());
        userDo.setLastLogin(Instant.now());
        mapper.updateById(userDo);

        UUID userId = UUID.fromString(userDo.getUuid());
        AccessTokenDTO accessTokenDTO = accessTokenManager.generate(userId, null);
        return new UserLoginDTO(userId, accessTokenDTO);
    }

    @Override
    protected UserDTO doToDto(UserDO record) {
        if (record == null) {
            return null;
        }
        UserDTO result = super.doToDto(record);
        result.setGeminiId(UUID.fromString(record.getGeminiId()));
        return result;
    }

    @Override
    protected UserDO dtoToDo(UserDTO record) {
        if (record == null) {
            return null;
        }
        UserDO result = super.dtoToDo(record);
        result.setGeminiId(record.getGeminiId().toString());
        return result;
    }

}
