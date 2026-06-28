package com.kuretru.web.libra.metadata.service.impl;

import com.kuretru.microservices.web.service.children.ChildrenOperator;
import com.kuretru.microservices.web.service.children.DefaultChildrenOperator;
import com.kuretru.microservices.web.service.impl.BaseSequencedServiceImpl;
import com.kuretru.web.libra.metadata.entity.data.MetadataTagSetItemDO;
import com.kuretru.web.libra.metadata.entity.mapper.MetadataTagSetItemEntityMapper;
import com.kuretru.web.libra.metadata.entity.query.MetadataTagSetItemQuery;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataTagSetItemDTO;
import com.kuretru.web.libra.metadata.mapper.MetadataTagSetItemMapper;
import com.kuretru.web.libra.metadata.service.MetadataTagSetItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetadataTagSetItemServiceImpl
        extends BaseSequencedServiceImpl<MetadataTagSetItemMapper, MetadataTagSetItemDO, MetadataTagSetItemDTO, MetadataTagSetItemQuery>
        implements MetadataTagSetItemService {

    private final ChildrenOperator<MetadataTagSetItemDTO, MetadataTagSetItemQuery> childrenOperator;

    @Autowired
    public MetadataTagSetItemServiceImpl(MetadataTagSetItemMapper mapper, MetadataTagSetItemEntityMapper entityMapper) {
        super(mapper, entityMapper);
        childrenOperator = new DefaultChildrenOperator<>(
                mapper, entityMapper,
                MetadataTagSetItemDO.class, MetadataTagSetItemDTO.class, MetadataTagSetItemQuery.class);
    }

    @Override
    public ChildrenOperator<MetadataTagSetItemDTO, MetadataTagSetItemQuery> childrenOperator() {
        return childrenOperator;
    }

}
