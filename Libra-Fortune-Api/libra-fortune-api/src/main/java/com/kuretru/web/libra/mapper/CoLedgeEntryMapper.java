package com.kuretru.web.libra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuretru.web.libra.entity.data.CoLedgerEntryDO;
import com.kuretru.web.libra.entity.data.CoLedgerUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CoLedgeEntryMapper extends BaseMapper<CoLedgerEntryDO> {

}