package com.kuretru.web.libra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuretru.web.libra.entity.data.LedgerDO;
import com.kuretru.web.libra.entity.data.SystemUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface LedgerMapper extends BaseMapper<LedgerDO> {

}