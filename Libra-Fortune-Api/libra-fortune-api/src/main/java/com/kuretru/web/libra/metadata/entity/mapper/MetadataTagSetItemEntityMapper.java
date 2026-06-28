package com.kuretru.web.libra.metadata.entity.mapper;


import com.kuretru.microservices.web.entity.mapper.BaseSequencedEntityMapper;
import com.kuretru.web.libra.metadata.entity.data.MetadataTagSetItemDO;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataTagSetItemDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MetadataTagSetItemEntityMapper extends BaseSequencedEntityMapper<MetadataTagSetItemDO, MetadataTagSetItemDTO> {

}
