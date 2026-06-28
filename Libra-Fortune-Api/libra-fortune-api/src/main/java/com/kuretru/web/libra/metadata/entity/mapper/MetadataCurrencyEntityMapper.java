package com.kuretru.web.libra.metadata.entity.mapper;

import com.kuretru.microservices.web.entity.mapper.BaseSequencedEntityMapper;
import com.kuretru.web.libra.metadata.entity.data.MetadataCurrencyDO;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataCurrencyDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MetadataCurrencyEntityMapper extends BaseSequencedEntityMapper<MetadataCurrencyDO, MetadataCurrencyDTO> {

}
