package com.kuretru.web.libra.metadata.entity.mapper;

import com.kuretru.microservices.web.v2.entity.mapper.BaseEntityMapper;
import com.kuretru.web.libra.metadata.entity.data.MetadataCurrencyDO;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataCurrencyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MetadataCurrencyEntityMapper extends BaseEntityMapper<MetadataCurrencyDO, MetadataCurrencyDTO> {

    @Override
    @Mapping(target = "sequence", ignore = true)
    MetadataCurrencyDO dtoToDo(MetadataCurrencyDTO record);

}
