package com.kuretru.web.libra.entity.mapper;

import com.kuretru.microservices.web.entity.mapper.BaseEntityMapper;
import com.kuretru.web.libra.entity.data.LedgerEntryDetailDO;
import com.kuretru.web.libra.entity.transfer.LedgerEntryDetailDTO;
import org.mapstruct.Mapper;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Mapper(componentModel = "spring")
public interface LedgerEntryDetailEntityMapper extends BaseEntityMapper<LedgerEntryDetailDO, LedgerEntryDetailDTO> {

}
