package com.kuretru.web.libra.ledger.entity.mapper;

import com.kuretru.microservices.web.v2.entity.mapper.BaseSequencedEntityMapper;
import com.kuretru.web.libra.ledger.entity.data.LedgerDO;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LedgerEntityMapper extends BaseSequencedEntityMapper<LedgerDO, LedgerDTO> {

    @Override
    @Mapping(target = "members", ignore = true)
    LedgerDTO doToDto(LedgerDO record);

}
