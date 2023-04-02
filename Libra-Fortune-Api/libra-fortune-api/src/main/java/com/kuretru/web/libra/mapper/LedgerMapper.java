package com.kuretru.web.libra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuretru.web.libra.entity.data.LedgerDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Repository
@Mapper
public interface LedgerMapper extends BaseMapper<LedgerDO> {

}
