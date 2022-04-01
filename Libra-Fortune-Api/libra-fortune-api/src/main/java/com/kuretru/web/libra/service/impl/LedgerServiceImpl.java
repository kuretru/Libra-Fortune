package com.kuretru.web.libra.service.impl;

import com.kuretru.microservices.common.utils.EnumUtils;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerDO;
import com.kuretru.web.libra.entity.enums.LedgerTypeEnum;
import com.kuretru.web.libra.entity.query.LedgerQuery;
import com.kuretru.web.libra.entity.transfer.CoLedgerUserDTO;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.mapper.LedgerMapper;
import com.kuretru.web.libra.service.CoLedgerUserService;
import com.kuretru.web.libra.service.LedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
public class LedgerServiceImpl extends BaseServiceImpl<LedgerMapper, LedgerDO, LedgerDTO, LedgerQuery> implements LedgerService {

    private final CoLedgerUserService coLedgerUserService;

    @Autowired
    public LedgerServiceImpl(LedgerMapper mapper, CoLedgerUserService coLedgerUserService) {
        super(mapper, LedgerDO.class, LedgerDTO.class, LedgerQuery.class);
        this.coLedgerUserService = coLedgerUserService;
    }

    @Override
    public synchronized LedgerDTO save(LedgerDTO record) throws ServiceException {
//        判断当前userId存在
        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
//        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e77a298e68");
        if (!(Arrays.asList(LedgerTypeEnum.values()).contains(record.getType()))) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "请输入正确的类型");
        }
        record.setOwnerId(userId);
        LedgerDTO ledgerDTO = super.save(record);
//        如果是合作账本 去coLedgerUserService账本那边再加一条
        if (record.getType().equals(LedgerTypeEnum.CO_COMMON) || record.getType().equals(LedgerTypeEnum.CO_FINANCIAL)) {
            CoLedgerUserDTO coLedgerUserDTO = new CoLedgerUserDTO();
            coLedgerUserDTO.setLedgerId(ledgerDTO.getId());
            coLedgerUserDTO.setUserId(userId);
            coLedgerUserDTO.setIsWritable(true);
            coLedgerUserService.save(coLedgerUserDTO);
        }
        return ledgerDTO;
    }


    @Override
    public LedgerDTO update(LedgerDTO record) throws ServiceException {
//        UUID userId = UUID.fromString("a087c0e3-2577-4a17-b435-7b12f7aa51e0");
        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");

//       判断账本存在
        LedgerDTO oldRecord = get(record.getId());
//        账户不存在
//        如果账本的owner不为当前用户
//        该用户改了这个账本的ownerId
        if (oldRecord == null || !oldRecord.getOwnerId().equals(userId) || !record.getOwnerId().equals(userId)) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
        }
        if (!oldRecord.getType().equals(record.getType()) || oldRecord.getOwnerId().equals(record.getOwnerId())) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账本类型不可变/账本拥有者不可变");
        }
        return super.update(record);
    }

//    @Override
//    先不管删除
//    public void remove(UUID uuid) throws ServiceException {
//        //        判断当前userId存在
//        UUID userId = UUID.fromString("56ec2b77-857f-435c-a44f-f6e74a298e68");
//        LedgerDTO exist = get(uuid);
////        账户不存在， 当前用户不存在，账户不属于当前登陆了用户
//        if (exist == null || userService.get(userId) == null || !exist.getOwnerId().equals(userId)) {
//            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
//        }
//
//        if (exist.getType().equals(LedgerTypeEnum.COMMON) || exist.getType().equals(LedgerTypeEnum.FINANCIAL)) {
////        单人账本/理财账本：如果账本的拥有者不为当前用户
//            super.remove(uuid);
//        } else {
////            合作账本去那边处理
////            删除先不管了
////            coLedgerUserService.remove(record);
//        }
//    }


    @Override
    protected LedgerDTO doToDto(LedgerDO record) {
        LedgerDTO result = super.doToDto(record);
        if (record != null) {
            result.setOwnerId(UUID.fromString(record.getOwnerId()));
            result.setType(EnumUtils.valueOf(LedgerTypeEnum.class, record.getType()));
        }
        return result;
    }

    @Override
    protected LedgerDO dtoToDo(LedgerDTO record) {
        LedgerDO result = super.dtoToDo(record);
        if (result != null) {
            result.setOwnerId(record.getOwnerId().toString());
            result.setType(record.getType().getCode());
        }
        return result;
    }

}
