package com.kuretru.web.libra.ledger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuretru.web.libra.ledger.entity.data.LedgerEntryTagDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository("ledgerV2EntryTagMapper")
@Mapper
public interface LedgerEntryTagMapper extends BaseMapper<LedgerEntryTagDO> {
}
