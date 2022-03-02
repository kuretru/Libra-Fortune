package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.api.common.constant.code.ServiceErrorCodes;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerDO;
import com.kuretru.web.libra.entity.data.SysUserDO;
import com.kuretru.web.libra.entity.query.LedgerQuery;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.entity.transfer.LedgerPermissionDTO;
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
    private final LedgerPermissionService ledgerPermissionService;
    private final SysUserService userService;

    @Autowired
    public LedgerServiceImpl(LedgerMapper mapper, LedgerPermissionService ledgerPermissionService, SysUserService userService) {
        super(mapper, LedgerDO.class, LedgerDTO.class, LedgerQuery.class);
        this.ledgerPermissionService = ledgerPermissionService;
        this.userService = userService;
    }

    public List<LedgerDTO> get(String userId) throws ServiceException.InternalServerError {
        QueryWrapper<SysUserDO> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("uuid", userId);
        SysUserDTO record = userService.get(userQueryWrapper);
        if (record == null) {
            throw new ServiceException.InternalServerError(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "用户不存在");
        }
        QueryWrapper<LedgerDO> ledgerQueryWrapper = new QueryWrapper<>();
        ledgerQueryWrapper.eq("user_id", userId);
        List<LedgerDO> result = mapper.selectList(ledgerQueryWrapper);
        return doToDto(result);
    }


    @Override
    public synchronized LedgerDTO save(LedgerDTO record) throws ServiceException {
        String userId = record.getUserId();
        if (userService.get(UUID.fromString(userId)) == null) {
            throw new ServiceException.InternalServerError(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "该用户不存在，不可操作");
        }
        UUID uuid = UUID.randomUUID();
        if (get(uuid) != null) {
            throw new ServiceException.InternalServerError(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "产生了已存在的UUID，请重新提交请求");
        }
        LedgerPermissionDTO ledgerPermissionDTO = new LedgerPermissionDTO();
        ledgerPermissionDTO.setLedgerId(uuid.toString());
        ledgerPermissionDTO.setUserId(record.getUserId());
        ledgerPermissionDTO.setReadable(1);
        ledgerPermissionDTO.setWritable(1);
        ledgerPermissionService.save(ledgerPermissionDTO);
        LedgerDO data = dtoToDo(record);
        addCreateTime(data, uuid);
        mapper.insert(data);
        return get(data.getId());
    }


}
