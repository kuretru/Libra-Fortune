package com.kuretru.web.libra.service.impl;

import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.impl.BaseServiceImpl;
import com.kuretru.api.common.util.EnumUtils;
import com.kuretru.web.libra.entity.data.LedgerDO;
import com.kuretru.web.libra.entity.enums.LedgerTypeEnum;
import com.kuretru.web.libra.entity.query.LedgerQuery;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.mapper.LedgerMapper;
import com.kuretru.web.libra.service.LedgerService;
import com.kuretru.web.libra.service.SystemUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
public class LedgerServiceImpl extends BaseServiceImpl<LedgerMapper, LedgerDO, LedgerDTO, LedgerQuery> implements LedgerService {
    public final SystemUserService userService;
//    public final CoLedgerUserServiceImpl coLedgerUserService;

    @Autowired
    public LedgerServiceImpl(LedgerMapper mapper, SystemUserService userService) {
        super(mapper, LedgerDO.class, LedgerDTO.class, LedgerQuery.class);
        this.userService = userService;
    }

    @Override
    public synchronized LedgerDTO save(LedgerDTO record) throws ServiceException {
//        判断当前userId存在
//        存在的
        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
//        不存在的
//        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e77a298e68");
        if (userService.get(userId) == null) {
            throw new ServiceException.NotFound(UserErrorCodes.REQUEST_PARAMETER_ERROR, "用户不存在");
        }
        if (!(Arrays.asList(LedgerTypeEnum.values()).contains(record.getType()))) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "请输入正确的类型");
        }
//        如果是合作账本 去coLedgerUserService账本那边
//        if (record.getType().equals(LedgerTypeEnum.CO_COMMON) || record.getType().equals(LedgerTypeEnum.CO_FINANCIAL)) {
//            return coLedgerUserService.save(record);
//        }
        record.setOwnerId(userId);
        return super.save(record);
    }

    @Override
    public LedgerDTO update(LedgerDTO record) throws ServiceException {
//        判断当前userId存在 别人的
//        UUID userId = UUID.fromString("a087c0e3-2577-4a17-b435-7b12f7aa51e0");
//        判断当前userId存在 自己的
        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
//        不存在的
//        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e77a298e68");
//       判断账本存在
        LedgerDTO exist = get(record.getId());
//        账户不存在， 当前用户不存在，账户不属于当前登陆了用户，是否修改了账户的主人
        if (exist == null || userService.get(userId) == null || !exist.getOwnerId().equals(userId) || !record.getOwnerId().equals(userId)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
        }

        if (!exist.getType().equals(record.getType())) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账本类型不可变");
        }

        if (record.getType().equals(LedgerTypeEnum.COMMON) || record.getType().equals(LedgerTypeEnum.FINANCIAL)) {
//        单人账本/理财账本：如果账本的拥有者不为当前用户
            return super.update(record);
        } else {
//            合作账本去那边处理
//            return coLedgerUserService.update(record);
        }

//        多人账本：拥有者不为当前用户 或者当前用户的 is writable = 0
//        if (exist.getType()== 8) {
//            return
//            查询co_ledger_user ledger_id +userId + is_writable = 1;
//            如果查不到报错
//            if (coLedgerUserService.getLedgerPermission(ledger_id, userId, isWritable) == null) {
//                throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
//            }
//        }
        return null;
    }

    @Override
    public void remove(UUID uuid) throws ServiceException {
        //        判断当前userId存在
        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
        LedgerDTO exist = get(uuid);
//        账户不存在， 当前用户不存在，账户不属于当前登陆了用户
        if (exist == null || userService.get(userId) == null || !exist.getOwnerId().equals(userId)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
        }

        if (exist.getType().equals(LedgerTypeEnum.COMMON) || exist.getType().equals(LedgerTypeEnum.FINANCIAL)) {
//        单人账本/理财账本：如果账本的拥有者不为当前用户
            super.remove(uuid);
        } else {
//            合作账本去那边处理
//            return coLedgerUserService.update(record);
        }
    }


    @Override
    protected LedgerDTO doToDto(LedgerDO record) {
        if (record == null) {
            return null;
        }
        LedgerDTO result = buildDTOInstance();
        BeanUtils.copyProperties(record, result);
        result.setId(UUID.fromString(record.getUuid()));
        result.setOwnerId(UUID.fromString(record.getOwnerId()));
        result.setType(EnumUtils.valueOf(LedgerTypeEnum.class, record.getType()));
        return result;
    }

    @Override
    protected LedgerDO dtoToDo(LedgerDTO record) {
        if (record == null) {
            return null;
        }
        LedgerDO result = buildDOInstance();
        BeanUtils.copyProperties(record, result);
        if (record.getId() != null) {
            result.setUuid(record.getId().toString());
        }
        result.setOwnerId(record.getOwnerId().toString());
        result.setType(record.getType().getCode());
        return result;
    }
}