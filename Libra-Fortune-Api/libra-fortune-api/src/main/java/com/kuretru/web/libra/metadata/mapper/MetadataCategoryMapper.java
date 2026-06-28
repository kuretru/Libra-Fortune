package com.kuretru.web.libra.metadata.mapper;

import com.kuretru.microservices.web.mapper.BaseSequencedMapper;
import com.kuretru.web.libra.metadata.entity.data.MetadataCategoryDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MetadataCategoryMapper extends BaseSequencedMapper<MetadataCategoryDO> {
}
