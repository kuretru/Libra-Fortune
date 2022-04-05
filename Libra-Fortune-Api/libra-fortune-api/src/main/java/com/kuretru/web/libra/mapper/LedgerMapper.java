package com.kuretru.web.libra.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.kuretru.web.libra.entity.data.LedgerDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface LedgerMapper extends BaseMapper<LedgerDO> {

    LedgerDO selectByUuid(@Param(Constants.WRAPPER) QueryWrapper<LedgerDO> queryWrapper);

}
