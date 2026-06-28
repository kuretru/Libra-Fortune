package com.kuretru.web.libra.ledger.mapper;

import com.kuretru.microservices.web.mapper.BaseSequencedMapper;
import com.kuretru.web.libra.ledger.entity.data.LedgerDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface LedgerMapper extends BaseSequencedMapper<LedgerDO> {
}
