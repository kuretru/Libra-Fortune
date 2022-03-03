package com.kuretru.web.libra.service.impl;

import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerDO;
import com.kuretru.web.libra.entity.query.LedgerQuery;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.entity.transfer.SysUserDTO;
import com.kuretru.web.libra.mapper.LedgerMapper;
import com.kuretru.web.libra.service.LedgerPermissionService;
import com.kuretru.web.libra.service.LedgerService;
import com.kuretru.web.libra.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LedgerServiceImpl extends BaseServiceImpl<LedgerMapper, LedgerDO, LedgerDTO, LedgerQuery> implements LedgerService {

    private final LedgerPermissionService permissionService;
    private final SysUserService userService;

    @Autowired
    public LedgerServiceImpl(LedgerMapper mapper, LedgerPermissionService permissionService, SysUserService userService) {
        super(mapper, LedgerDO.class, LedgerDTO.class, LedgerQuery.class);
        this.permissionService = permissionService;
        this.userService = userService;
    }

    @Override
    public List<LedgerDTO> list(UUID userId) throws ServiceException {
        SysUserDTO userRecord = userService.get(userId);
        if (userRecord == null) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "该用户不存在");
        }

        List<LedgerDO> result = mapper.listByUserId(userId.toString());
        return doToDto(result);
    }

}
