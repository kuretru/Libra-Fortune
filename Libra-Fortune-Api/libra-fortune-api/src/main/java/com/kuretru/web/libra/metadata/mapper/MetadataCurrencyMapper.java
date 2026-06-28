package com.kuretru.web.libra.metadata.mapper;

import com.kuretru.microservices.web.mapper.BaseSequencedMapper;
import com.kuretru.web.libra.metadata.entity.data.MetadataCurrencyDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MetadataCurrencyMapper extends BaseSequencedMapper<MetadataCurrencyDO> {
}
