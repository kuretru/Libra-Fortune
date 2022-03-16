package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.api.common.constant.code.ServiceErrorCodes;
import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.CoLedgerUserDO;
import com.kuretru.web.libra.entity.query.CoLedgerUserQuery;
import com.kuretru.web.libra.entity.transfer.CoLedgerUserDTO;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.entity.transfer.SystemUserDTO;
import com.kuretru.web.libra.mapper.CoLedgerUserMapper;
import com.kuretru.web.libra.service.CoLedgerUserService;
import com.kuretru.web.libra.service.LedgerService;
import com.kuretru.web.libra.service.SystemUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class CoLedgerUserServiceImpl extends BaseServiceImpl<CoLedgerUserMapper, CoLedgerUserDO, CoLedgerUserDTO, CoLedgerUserQuery> implements CoLedgerUserService {
    private final SystemUserService userService;
    private final LedgerService ledgerService;

    @Autowired
    public CoLedgerUserServiceImpl(CoLedgerUserMapper mapper, SystemUserService userService, @Lazy LedgerService ledgerService) {
        super(mapper, CoLedgerUserDO.class, CoLedgerUserDTO.class, CoLedgerUserQuery.class);
        this.userService = userService;
        this.ledgerService = ledgerService;
    }

    /**
     * 给合作账本添加合作人
     */
    public synchronized CoLedgerUserDTO save(CoLedgerUserDTO record) throws ServiceException {
        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
        LedgerDTO ledgerRecord = ledgerService.get(record.getLedgerId());
        SystemUserDTO userRecord = userService.get(record.getUserId());
        if (userRecord == null || ledgerRecord == null || !ledgerRecord.getOwnerId().equals(userId)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "用户无权限");
        }
        QueryWrapper<CoLedgerUserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ledger_id", record.getLedgerId().toString());
        queryWrapper.eq("user_id", record.getUserId().toString());
        if (!list(queryWrapper).isEmpty()) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可重复添加");
        }
        return super.save(record);
    }

    @Override
    public CoLedgerUserDTO update(CoLedgerUserDTO record) throws ServiceException {
//        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
        UUID userId = UUID.fromString("a087c0e3-2577-4a17-b435-7b12f7aa51e0");
        CoLedgerUserDTO oldCoLedgerUser = get(record.getId());
        LedgerDTO ledger = ledgerService.get(record.getLedgerId());
//        这条记录不存在 || 更改了这条记录的ledgerId/userId || 这条记录账本的ownerId才可以添加
        if (oldCoLedgerUser == null || !oldCoLedgerUser.getLedgerId().equals(record.getLedgerId()) || !oldCoLedgerUser.getUserId().equals(record.getUserId()) || !ledger.getOwnerId().equals(userId)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操作");
        }
//        不要更改自己的权限
        if (ledger.getOwnerId().equals(record.getUserId())) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不要更改自己的权限");
        }
        CoLedgerUserDO data = dtoToDo(record);
        data.setUpdateTime(Instant.now());
        QueryWrapper<CoLedgerUserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", data.getUuid());
        int rows = mapper.update(data, queryWrapper);
        if (0 == rows) {
            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "指定资源不存在");
        } else if (1 != rows) {
            throw new ServiceException.InternalServerError(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "发现多个相同业务主键");
        }
        return get(record.getId());
    }

    @Override
    public Boolean getLedgerPermission(UUID ledgerId, UUID userId, boolean isWritable) {
        QueryWrapper<CoLedgerUserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ledger_id", ledgerId.toString());
        queryWrapper.eq("user_id", userId.toString());
        queryWrapper.eq("is_writable", isWritable);
        return mapper.exists(queryWrapper);
    }


//    @Override
//    删除先不管
//    public void remove(UUID uuid) throws ServiceException {
//        super.remove(uuid);
//    }

    @Override
    protected CoLedgerUserDTO doToDto(CoLedgerUserDO record) {
        if (record == null) {
            return null;
        }
        CoLedgerUserDTO result = buildDTOInstance();
        BeanUtils.copyProperties(record, result);
        result.setId(UUID.fromString(record.getUuid()));
        result.setUserId(UUID.fromString(record.getUserId()));
        result.setLedgerId(UUID.fromString(record.getLedgerId()));
        return result;
    }

    @Override
    protected CoLedgerUserDO dtoToDo(CoLedgerUserDTO record) {
        if (record == null) {
            return null;
        }
        CoLedgerUserDO result = buildDOInstance();
        BeanUtils.copyProperties(record, result);
        if (record.getId() != null) {
            result.setUuid(record.getId().toString());
        }
//        不知道是否需要判断 可以回头再看
//        if (record.getLedgerId() != null) {
        result.setLedgerId(record.getLedgerId().toString());
//        }
        result.setUserId(record.getUserId().toString());
        return result;
    }


}