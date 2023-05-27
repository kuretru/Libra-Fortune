package com.kuretru.web.libra.entity.mapper;

import com.kuretru.microservices.web.entity.mapper.BaseSequenceEntityMapper;
import com.kuretru.web.libra.entity.data.LedgerTagDO;
import com.kuretru.web.libra.entity.transfer.LedgerTagDTO;
import com.kuretru.web.libra.entity.view.LedgerTagVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Mapper(componentModel = "spring")
public interface LedgerTagEntityMapper extends BaseSequenceEntityMapper<LedgerTagDO, LedgerTagDTO> {

    /**
     * 将DO转换为VO
     *
     * @param record DO
     * @return VO
     */
    @Mapping(source = "uuid", target = "id")
    LedgerTagVO doToVo(LedgerTagDO record);

    /**
     * 将DO批量转换为VO
     *
     * @param records DO列表
     * @return VO列表
     */
    List<LedgerTagVO> doToVo(List<LedgerTagDO> records);

}
