package com.kuretru.web.libra.service;

import com.kuretru.microservices.authentication.entity.UserLoginDTO;
import com.kuretru.microservices.oauth2.common.entity.GalaxyUserDTO;
import com.kuretru.microservices.web.exception.ServiceException;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param record 登录请求实体
     * @return 登录响应实体
     * @throws ServiceException 登录失败时会产生异常
     */
    UserLoginDTO login(GalaxyUserDTO record) throws ServiceException;

}
