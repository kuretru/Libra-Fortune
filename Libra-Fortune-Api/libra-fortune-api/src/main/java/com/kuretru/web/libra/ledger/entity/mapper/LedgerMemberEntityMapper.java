package com.kuretru.web.libra.ledger.entity.mapper;

import com.kuretru.microservices.web.v2.entity.mapper.BaseEntityMapper;
import com.kuretru.web.libra.ledger.entity.data.LedgerMemberDO;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerMemberDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LedgerMemberEntityMapper extends BaseEntityMapper<LedgerMemberDO, LedgerMemberDTO> {

}
