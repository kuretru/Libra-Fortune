package com.kuretru.web.libra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuretru.web.libra.entity.data.LedgerCategoryDO;
import com.kuretru.web.libra.entity.data.LedgerDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface LedgerCategoryMapper extends BaseMapper<LedgerCategoryDO> {

}