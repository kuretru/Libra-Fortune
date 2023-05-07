package com.kuretru.web.libra.entity.mapper;

import com.kuretru.microservices.common.utils.EnumUtils;
import com.kuretru.microservices.web.entity.mapper.BaseEntityMapper;
import com.kuretru.web.libra.entity.business.LedgerBO;
import com.kuretru.web.libra.entity.data.LedgerDO;
import com.kuretru.web.libra.entity.enums.LedgerTypeEnum;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.entity.view.LedgerVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

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

    /**
     * 将BO转换为VO
     *
     * @param record BO
     * @return VO
     */
    @Mapping(source = "uuid", target = "id")
    LedgerVO boToVo(LedgerBO record);

    /**
     * 将BO批量转换为VO
     *
     * @param records BO列表
     * @return VO列表
     */
    List<LedgerVO> boToVo(List<LedgerBO> records);

}
