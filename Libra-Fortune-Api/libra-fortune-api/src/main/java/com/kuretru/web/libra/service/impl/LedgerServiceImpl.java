package com.kuretru.web.libra.service.impl;

import com.kuretru.microservices.authentication.context.AccessTokenContext;
import com.kuretru.microservices.web.constant.code.ServiceErrorCodes;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerDO;
import com.kuretru.web.libra.entity.query.LedgerQuery;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.mapper.LedgerMapper;
import com.kuretru.web.libra.service.LedgerService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Service
public class LedgerServiceImpl extends BaseServiceImpl<LedgerMapper, LedgerDO, LedgerDTO, LedgerQuery> implements LedgerService {

    @Autowired
    public LedgerServiceImpl(LedgerMapper mapper, LedgerEntityMapper entityMapper) {
        super(mapper, entityMapper);
    }

    @Override
    public LedgerDTO save(LedgerDTO record) throws ServiceException {
        if (!record.getOwnerId().equals(AccessTokenContext.getUserId())) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账本所属用户必须为当前登录用户");
        }
        return super.save(record);
    }

    @Override
    public LedgerDTO update(LedgerDTO record) throws ServiceException {
        LedgerDTO old = get(record.getId());
        if (old == null) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "该账本不存在");
        } else if (!old.getOwnerId().equals(AccessTokenContext.getUserId())) {
            throw new ServiceException(UserErrorCodes.ACCESS_UNAUTHORIZED, "只有账本拥有者可以修改账本");
        } else if (!record.getOwnerId().equals(old.getOwnerId())) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "暂不可修改账本拥有者");
        } else if (!record.getType().equals(old.getType())) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "暂不可修改账本类型");
        }
        return super.update(record);
    }

    @Override
    public void remove(UUID uuid) throws ServiceException {
        throw ServiceException.build(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "尚未实现");
    }

    @Override
    public void verifyIamLedgerOwner(UUID ledgerId) throws ServiceException {
        LedgerDTO ledgerDTO = get(ledgerId);
        if (ledgerDTO == null) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "指定账本不存在");
        }
        UUID myUserId = AccessTokenContext.getUserId();
        if (!ledgerDTO.getOwnerId().equals(myUserId)) {
            throw ServiceException.build(UserErrorCodes.ACCESS_PERMISSION_ERROR, "账本拥有者才可以修改账本成员");
        }
    }

    @Mapper(componentModel = "spring")
    interface LedgerEntityMapper extends BaseServiceImpl.BaseEntityMapper<LedgerDO, LedgerDTO> {

        /**
         * 将数据实体转换为数据传输实体
         *
         * @param record 数据实体
         * @return 数据传输实体
         */
        @Mapping(source = "uuid", target = "id")
        @Mapping(source = "com.kuretru.microservices.common.utils.EnumUtils.valueOf(com.kuretru.web.libra.entity.enums.LedgerTypeEnum.class, type)", target = "type")
        LedgerDTO doToDto(LedgerDO record);

        /**
         * 将数据传输实体转换为数据实体
         *
         * @param record 数据传输实体
         * @return 数据实体
         */
        @Mapping(source = "id", target = "uuid")
        @Mapping(target = "id", ignore = true)
        @Mapping(target = "createTime", ignore = true)
        @Mapping(target = "updateTime", ignore = true)
        @Mapping(source = "type.getCode()", target = "type")
        LedgerDO dtoToDo(LedgerDTO record);

    }

}
