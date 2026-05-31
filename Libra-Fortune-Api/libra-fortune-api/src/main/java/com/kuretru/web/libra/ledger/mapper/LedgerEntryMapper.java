package com.kuretru.web.libra.ledger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuretru.web.libra.ledger.entity.data.LedgerEntryDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository("ledgerV2EntryMapper")
@Mapper
public interface LedgerEntryMapper extends BaseMapper<LedgerEntryDO> {
}
