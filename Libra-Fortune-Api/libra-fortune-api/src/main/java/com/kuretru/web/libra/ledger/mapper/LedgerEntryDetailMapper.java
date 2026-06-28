package com.kuretru.web.libra.ledger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuretru.web.libra.ledger.entity.data.LedgerEntryDetailDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface LedgerEntryDetailMapper extends BaseMapper<LedgerEntryDetailDO> {
}
