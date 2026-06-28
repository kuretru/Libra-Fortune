package com.kuretru.web.libra.metadata.entity.mapper;

import com.kuretru.microservices.web.entity.mapper.BaseSequencedEntityMapper;
import com.kuretru.web.libra.metadata.entity.data.MetadataTagSetDO;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataTagSetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MetadataTagSetEntityMapper extends BaseSequencedEntityMapper<MetadataTagSetDO, MetadataTagSetDTO> {

    @Override
    @Mapping(target = "items", ignore = true)
    MetadataTagSetDTO doToDto(MetadataTagSetDO record);

}
