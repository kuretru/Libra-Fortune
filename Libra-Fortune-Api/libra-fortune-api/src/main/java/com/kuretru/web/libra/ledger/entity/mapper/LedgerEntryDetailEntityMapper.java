package com.kuretru.web.libra.ledger.entity.mapper;

import com.kuretru.microservices.web.v2.entity.mapper.BaseEntityMapper;
import com.kuretru.web.libra.ledger.entity.data.LedgerEntryDetailDO;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerEntryDetailDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", implementationName = "LedgerV2EntryDetailEntityMapperImpl")
public interface LedgerEntryDetailEntityMapper extends BaseEntityMapper<LedgerEntryDetailDO, LedgerEntryDetailDTO> {

}
