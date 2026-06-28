package com.kuretru.web.libra.metadata.service;

import com.kuretru.microservices.web.service.BaseService;
import com.kuretru.microservices.web.service.ChildrenCapable;
import com.kuretru.microservices.web.service.SequencedService;
import com.kuretru.web.libra.metadata.entity.query.MetadataTagSetItemQuery;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataTagSetItemDTO;

public interface MetadataTagSetItemService extends
        BaseService<MetadataTagSetItemDTO, MetadataTagSetItemQuery>,
        SequencedService<MetadataTagSetItemDTO>,
        ChildrenCapable<MetadataTagSetItemDTO, MetadataTagSetItemQuery> {

}
