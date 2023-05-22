package com.kuretru.web.libra.entity.mapper;

import com.kuretru.microservices.web.entity.mapper.BaseEntityMapper;
import com.kuretru.web.libra.entity.business.LedgerEntryBO;
import com.kuretru.web.libra.entity.data.LedgerEntryDO;
import com.kuretru.web.libra.entity.transfer.LedgerEntryDTO;
import com.kuretru.web.libra.entity.view.LedgerEntryVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Mapper(componentModel = "spring", uses = LedgerEntryDetailEntityMapper.class)
public interface LedgerEntryEntityMapper extends BaseEntityMapper<LedgerEntryDO, LedgerEntryDTO> {

    /**
     * 将BO转换为VO
     *
     * @param record BO
     * @return VO
     */
    @Mapping(source = "uuid", target = "id")
    LedgerEntryVO boToVo(LedgerEntryBO record);

    /**
     * 将BO批量转换为VO
     *
     * @param records BO列表
     * @return VO列表
     */
    List<LedgerEntryVO> boToVo(List<LedgerEntryBO> records);

}
