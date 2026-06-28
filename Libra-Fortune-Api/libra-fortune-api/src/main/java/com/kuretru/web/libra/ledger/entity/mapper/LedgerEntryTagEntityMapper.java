package com.kuretru.web.libra.ledger.entity.mapper;

import com.kuretru.microservices.web.v2.entity.mapper.BaseEntityMapper;
import com.kuretru.web.libra.ledger.entity.data.LedgerEntryTagDO;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerEntryTagDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LedgerEntryTagEntityMapper extends BaseEntityMapper<LedgerEntryTagDO, LedgerEntryTagDTO> {
}
