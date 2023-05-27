package com.kuretru.web.libra.mapper;

import com.kuretru.microservices.web.mapper.BaseSequenceMapper;
import com.kuretru.web.libra.entity.data.LedgerTagDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Repository
@Mapper
public interface LedgerTagMapper extends BaseSequenceMapper<LedgerTagDO> {

}
