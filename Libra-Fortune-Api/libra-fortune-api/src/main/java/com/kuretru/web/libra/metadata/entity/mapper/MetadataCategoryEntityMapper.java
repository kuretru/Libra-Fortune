package com.kuretru.web.libra.metadata.entity.mapper;

import com.kuretru.microservices.web.v2.entity.mapper.BaseEntityMapper;
import com.kuretru.web.libra.metadata.entity.data.MetadataCategoryDO;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataCategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MetadataCategoryEntityMapper extends BaseEntityMapper<MetadataCategoryDO, MetadataCategoryDTO> {

    @Override
    @Mapping(target = "sequence", ignore = true)
    MetadataCategoryDO dtoToDo(MetadataCategoryDTO record);

}
