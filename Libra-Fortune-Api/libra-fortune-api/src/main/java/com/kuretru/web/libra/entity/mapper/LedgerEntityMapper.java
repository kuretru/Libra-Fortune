package com.kuretru.web.libra.entity.mapper;

import com.kuretru.microservices.common.utils.EnumUtils;
import com.kuretru.microservices.web.entity.mapper.BaseEntityMapper;
import com.kuretru.web.libra.entity.data.LedgerDO;
import com.kuretru.web.libra.entity.enums.LedgerTypeEnum;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import org.mapstruct.Mapper;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */

@Mapper(componentModel = "spring")
public interface LedgerEntityMapper extends BaseEntityMapper<LedgerDO, LedgerDTO> {

    default LedgerTypeEnum toLedgerTypeEnum(Short code) {
        return EnumUtils.valueOf(LedgerTypeEnum.class, code);
    }

    default Short fromLedgerTypeEnum(LedgerTypeEnum type) {
        return type.getCode();
    }

}
