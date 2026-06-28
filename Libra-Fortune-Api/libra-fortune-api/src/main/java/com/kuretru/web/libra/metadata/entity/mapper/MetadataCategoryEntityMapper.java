package com.kuretru.web.libra.metadata.entity.mapper;

import com.kuretru.microservices.web.entity.mapper.BaseSequencedEntityMapper;
import com.kuretru.web.libra.metadata.entity.data.MetadataCategoryDO;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataCategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MetadataCategoryEntityMapper extends BaseSequencedEntityMapper<MetadataCategoryDO, MetadataCategoryDTO> {


    @Override
    @Mapping(target = "children", ignore = true)
    MetadataCategoryDTO doToDto(MetadataCategoryDO record);

}
