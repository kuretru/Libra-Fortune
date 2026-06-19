package com.kuretru.web.libra.metadata.service;

import com.kuretru.microservices.web.v2.entity.query.EmptyQuery;
import com.kuretru.microservices.web.v2.service.BaseService;
import com.kuretru.microservices.web.v2.service.ChildrenCapable;
import com.kuretru.microservices.web.v2.service.SequencedService;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataTagSetItemDTO;

public interface MetadataTagSetItemService extends
        BaseService<MetadataTagSetItemDTO, EmptyQuery>,
        SequencedService<MetadataTagSetItemDTO>,
        ChildrenCapable<MetadataTagSetItemDTO> {

}
