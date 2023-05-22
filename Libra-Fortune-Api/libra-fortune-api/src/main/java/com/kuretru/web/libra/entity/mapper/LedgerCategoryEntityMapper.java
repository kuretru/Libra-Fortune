package com.kuretru.web.libra.entity.mapper;

import com.kuretru.microservices.web.entity.mapper.BaseEntityMapper;
import com.kuretru.web.libra.entity.data.LedgerCategoryDO;
import com.kuretru.web.libra.entity.transfer.LedgerCategoryDTO;
import com.kuretru.web.libra.entity.view.LedgerCategoryVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Mapper(componentModel = "spring")
public interface LedgerCategoryEntityMapper extends BaseEntityMapper<LedgerCategoryDO, LedgerCategoryDTO> {

    /**
     * 将DO转换为VO
     *
     * @param record DO
     * @return VO
     */
    @Mapping(source = "uuid", target = "id")
    LedgerCategoryVO doToVo(LedgerCategoryDO record);

    /**
     * 将DO批量转换为VO
     *
     * @param records DO列表
     * @return VO列表
     */
    List<LedgerCategoryVO> doToVo(List<LedgerCategoryDO> records);

}
