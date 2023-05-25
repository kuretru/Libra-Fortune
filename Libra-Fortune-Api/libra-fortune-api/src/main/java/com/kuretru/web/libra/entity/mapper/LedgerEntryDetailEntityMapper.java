package com.kuretru.web.libra.entity.mapper;

import com.kuretru.microservices.web.entity.mapper.BaseEntityMapper;
import com.kuretru.web.libra.entity.business.LedgerEntryDetailBO;
import com.kuretru.web.libra.entity.data.LedgerEntryDetailDO;
import com.kuretru.web.libra.entity.transfer.LedgerEntryDetailDTO;
import com.kuretru.web.libra.entity.view.LedgerEntryDetailVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Mapper(componentModel = "spring")
public interface LedgerEntryDetailEntityMapper extends BaseEntityMapper<LedgerEntryDetailDO, LedgerEntryDetailDTO> {

    /**
     * 将BO转换为VO
     *
     * @param record BO
     * @return VO
     */
    @Mapping(source = "uuid", target = "id")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "paymentChannel", ignore = true)
    @Mapping(target = "tags", ignore = true)
    LedgerEntryDetailVO boToVo(LedgerEntryDetailBO record);

    /**
     * 将BO批量转换为VO
     *
     * @param records BO列表
     * @return VO列表
     */
    List<LedgerEntryDetailVO> boToVo(List<LedgerEntryDetailBO> records);

}
