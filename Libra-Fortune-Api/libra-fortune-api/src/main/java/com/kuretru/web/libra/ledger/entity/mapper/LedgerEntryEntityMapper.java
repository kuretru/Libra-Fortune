package com.kuretru.web.libra.ledger.entity.mapper;

import com.kuretru.microservices.web.v2.entity.mapper.BaseEntityMapper;
import com.kuretru.web.libra.ledger.entity.data.LedgerEntryDO;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerEntryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", implementationName = "LedgerV2EntryEntityMapperImpl")
public interface LedgerEntryEntityMapper extends BaseEntityMapper<LedgerEntryDO, LedgerEntryDTO> {

    @Override
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "details", ignore = true)
    LedgerEntryDTO doToDto(LedgerEntryDO record);

}
