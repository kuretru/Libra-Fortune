package com.kuretru.web.libra.metadata.entity.mapper;


import com.kuretru.microservices.web.v2.entity.mapper.BaseEntityMapper;
import com.kuretru.web.libra.metadata.entity.data.MetadataTagSetItemDO;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataTagSetItemDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MetadataTagSetItemEntityMapper extends BaseEntityMapper<MetadataTagSetItemDO, MetadataTagSetItemDTO> {

}
