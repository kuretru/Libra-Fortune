package com.kuretru.web.libra.entity.mapper;

import com.kuretru.microservices.web.entity.mapper.BaseEntityMapper;
import com.kuretru.web.libra.entity.data.LedgerEntryDetailTagDO;
import com.kuretru.web.libra.entity.transfer.LedgerEntryDetailTagDTO;
import org.mapstruct.Mapper;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Mapper(componentModel = "spring")
public interface LedgerEntryDetailTagEntityMapper extends BaseEntityMapper<LedgerEntryDetailTagDO, LedgerEntryDetailTagDTO> {

}
