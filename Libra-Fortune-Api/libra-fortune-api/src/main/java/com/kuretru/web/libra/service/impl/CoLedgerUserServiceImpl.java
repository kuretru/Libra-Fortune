package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.CoLedgerUserDO;
import com.kuretru.web.libra.entity.query.CoLedgerUserQuery;
import com.kuretru.web.libra.entity.transfer.CoLedgerUserDTO;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.mapper.CoLedgerUserMapper;
import com.kuretru.web.libra.service.CoLedgerUserService;
import com.kuretru.web.libra.service.LedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CoLedgerUserServiceImpl extends BaseServiceImpl<CoLedgerUserMapper, CoLedgerUserDO, CoLedgerUserDTO, CoLedgerUserQuery> implements CoLedgerUserService {
    private final LedgerService ledgerService;

    @Autowired
    public CoLedgerUserServiceImpl(CoLedgerUserMapper mapper, @Lazy LedgerService ledgerService) {
        super(mapper, CoLedgerUserDO.class, CoLedgerUserDTO.class, CoLedgerUserQuery.class);
        this.ledgerService = ledgerService;
    }

    /**
     * 给合作账本添加合作人
     */
    public synchronized CoLedgerUserDTO save(CoLedgerUserDTO record) throws ServiceException {
//        UUID userId = UUID.fromString("a087c0e3-2577-4a17-b435-7b12f7aa51e0");
        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
//        UUID userId = UUID.fromString("a7f39ae9-8a75-4914-8737-3f6a979ebb92");
        LedgerDTO existLedger = ledgerService.get(record.getLedgerId());
        if (existLedger == null || !existLedger.getOwnerId().equals(userId)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "ledger不存在/user无权添加");
        }
        QueryWrapper<CoLedgerUserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ledger_id", record.getLedgerId().toString());
        queryWrapper.eq("user_id", record.getUserId().toString());
        if (mapper.exists(queryWrapper)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可重复添加");
        }
        return super.save(record);
    }

    @Override
    public CoLedgerUserDTO update(CoLedgerUserDTO record) throws ServiceException {
//        UUID userId = UUID.fromString("a087c0e3-2577-4a17-b435-7b12f7aa51e0");
        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
//        UUID userId = UUID.fromString("a7f39ae9-8a75-4914-8737-3f6a979ebb92");
        CoLedgerUserDTO oldRecord = get(record.getId());
//        这条记录不存在
        if (oldRecord == null) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "该记录不存在");
        }
//        这条记录账本的ownerId才可以更新合作人信息
        LedgerDTO ledger = ledgerService.get(record.getLedgerId());

        if (!ledger.getOwnerId().equals(userId)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "无权修改");
        }
//        owner不要更改自己的权限
        if (ledger.getOwnerId().equals(record.getUserId())) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "owner不要更改自己的权限");
        }
//        更改了这条记录的ledgerId/userId
        if (!oldRecord.getLedgerId().equals(record.getLedgerId()) || !oldRecord.getUserId().equals(record.getUserId())) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可修改ledgerId和userId");
        }
        return super.update(record);
    }

    @Override
    public void remove(UUID uuid) throws ServiceException {
        UUID userId = UUID.fromString("a087c0e3-2577-4a17-b435-7b12f7aa51e0");
//        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
//        UUID userId = UUID.fromString("a7f39ae9-8a75-4914-8737-3f6a979ebb92");
        CoLedgerUserDTO oldRecord = get(uuid);
//        这条记录不存在
        if (oldRecord == null) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "该记录不存在");
        }
        LedgerDTO ledger = ledgerService.get(oldRecord.getLedgerId());

//        owner不要删除自己
        if (ledger.getOwnerId().equals(oldRecord.getUserId())) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不要删除自己");
        }
//        自己可以删除自己
//        owner可以删除别人    自己在该账本里可以退出
        if (ledger.getOwnerId().equals(userId) || getDeletePermission(ledger.getId(), userId)) {
            super.remove(uuid);
            return;
        }
        throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "无权删除");
    }

    @Override
    public Boolean getLedgerPermission(UUID ledgerId, UUID userId, boolean isWritable) {
        QueryWrapper<CoLedgerUserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ledger_id", ledgerId.toString());
        queryWrapper.eq("user_id", userId.toString());
        queryWrapper.eq("is_writable", isWritable);
        return mapper.exists(queryWrapper);
    }

    private Boolean getDeletePermission(UUID ledgerId, UUID userId) {
        QueryWrapper<CoLedgerUserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ledger_id", ledgerId.toString());
        queryWrapper.eq("user_id", userId.toString());
        return mapper.exists(queryWrapper);
    }

    @Override
    protected CoLedgerUserDTO doToDto(CoLedgerUserDO record) {
        CoLedgerUserDTO result = super.doToDto(record);
        if (record != null) {
            result.setUserId(UUID.fromString(record.getUserId()));
            result.setLedgerId(UUID.fromString(record.getLedgerId()));
        }
        return result;
    }

    @Override
    protected CoLedgerUserDO dtoToDo(CoLedgerUserDTO record) {
        CoLedgerUserDO result = super.dtoToDo(record);
        if (result != null) {
            result.setLedgerId(record.getLedgerId().toString());
            result.setUserId(record.getUserId().toString());
        }
        return result;
    }


}