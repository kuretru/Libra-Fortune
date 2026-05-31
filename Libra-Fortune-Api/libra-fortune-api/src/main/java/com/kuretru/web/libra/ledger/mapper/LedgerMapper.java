package com.kuretru.web.libra.ledger.mapper;

import com.kuretru.microservices.web.v2.mapper.BaseSequencedMapper;
import com.kuretru.web.libra.ledger.entity.data.LedgerDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository("ledgerV2Mapper")
@Mapper
public interface LedgerMapper extends BaseSequencedMapper<LedgerDO> {
}
