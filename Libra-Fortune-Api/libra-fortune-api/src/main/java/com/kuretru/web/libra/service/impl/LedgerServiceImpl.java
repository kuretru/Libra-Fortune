package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.authentication.context.AccessTokenContext;
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
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public synchronized LedgerDTO save(LedgerDTO record) throws ServiceException {
        // 判断当前userId存在
        UUID userId = AccessTokenContext.getUserId();
        if (!userId.equals(record.getOwnerId())) {
            throw new ServiceException(UserErrorCodes.ACCESS_UNAUTHORIZED, "请勿操作别人的数据");
        }
        LedgerDTO ledgerDTO = super.save(record);
        // 去coLedgerUserService账本那边再加一条
        CoLedgerUserDTO coLedgerUserDTO = new CoLedgerUserDTO();
        coLedgerUserDTO.setLedgerId(ledgerDTO.getId());
        coLedgerUserDTO.setUserId(userId);
        coLedgerUserDTO.setIsWritable(true);
        coLedgerUserService.save(coLedgerUserDTO);
        return ledgerDTO;
    }

    @Override
    public LedgerDTO update(LedgerDTO record) throws ServiceException {
        UUID userId = AccessTokenContext.getUserId();
//       判断账本存在
        LedgerDTO oldRecord = get(record.getId());
//        账户不存在
//        如果账本的owner不为当前用户
//        该用户改了这个账本的ownerId
        if (oldRecord == null || !oldRecord.getOwnerId().equals(userId) || !record.getOwnerId().equals(userId)) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
        }
        if (!oldRecord.getType().equals(record.getType())) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账本类型不可变");
        }
        return super.update(record);
    }

    @Override
    //先不管删除
    public void remove(UUID uuid) throws ServiceException {
        throw new NotImplementedException();
//        // 判断当前userId存在
//        UUID userId = AccessTokenContext.getUserId();
//        LedgerDTO exist = get(uuid);
//        //  账户不存在， 当前用户不存在，账户不属于当前登陆了用户
//        if (exist == null || userService.get(userId) == null || !exist.getOwnerId().equals(userId)) {
//            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不可操做");
//        }
    }

    @Override
    public LedgerDTO get(UUID uuid) {
        QueryWrapper<LedgerDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", uuid.toString());
        LedgerDO record = mapper.selectByUuid(queryWrapper);
        return doToDto(record);
    }

    @Override
    public LedgerDTO get(UUID uuid, UUID userId) {
        QueryWrapper<LedgerDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("co.ledger_id", uuid.toString());
        queryWrapper.eq("co.user_id", userId.toString());
        LedgerDO record = mapper.selectOne(queryWrapper);
        return doToDto(record);
    }

    @Override
    protected QueryWrapper<LedgerDO> buildQueryWrapper(LedgerQuery query) {
        UUID userId = AccessTokenContext.getUserId();
        QueryWrapper<LedgerDO> queryWrapper = super.buildQueryWrapper(query);
        queryWrapper.eq("co.user_id", userId.toString());
        return queryWrapper;
    }

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
