package com.kuretru.web.libra.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.BaseService;
import com.kuretru.web.libra.entity.data.LedgerDO;
import com.kuretru.web.libra.entity.data.SysUserDO;
import com.kuretru.web.libra.entity.query.LedgerCategoryQuery;
import com.kuretru.web.libra.entity.query.SysUserQuery;
import com.kuretru.web.libra.entity.transfer.LedgerCategoryDTO;
import com.kuretru.web.libra.entity.transfer.SysUserDTO;

import java.util.List;


public interface SysUserService extends BaseService<SysUserDTO, SysUserQuery> {
    SysUserDTO get(String username,String password) throws ServiceException.InternalServerError;

    SysUserDTO get(QueryWrapper<SysUserDO> userQueryWrapper);
}
