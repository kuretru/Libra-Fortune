package com.kuretru.web.libra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuretru.web.libra.entity.data.CoLedgerUserDO;
import com.kuretru.web.libra.entity.data.LedgerEntryDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CoLedgerUserMapper extends BaseMapper<CoLedgerUserDO> {

}