package com.kuretru.web.libra.metadata.service.impl;

import com.kuretru.microservices.web.v2.entity.query.EmptyQuery;
import com.kuretru.microservices.web.v2.service.ability.children.ChildrenOperator;
import com.kuretru.microservices.web.v2.service.ability.children.DefaultChildrenOperator;
import com.kuretru.microservices.web.v2.service.impl.BaseSequencedServiceImpl;
import com.kuretru.web.libra.metadata.entity.data.MetadataTagSetItemDO;
import com.kuretru.web.libra.metadata.entity.mapper.MetadataTagSetItemEntityMapper;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataTagSetItemDTO;
import com.kuretru.web.libra.metadata.mapper.MetadataTagSetItemMapper;
import com.kuretru.web.libra.metadata.service.MetadataTagSetItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetadataTagSetItemServiceImpl
        extends BaseSequencedServiceImpl<MetadataTagSetItemMapper, MetadataTagSetItemDO, MetadataTagSetItemDTO, EmptyQuery>
        implements MetadataTagSetItemService {

    private final ChildrenOperator<MetadataTagSetItemDTO> childrenOperator;

    @Autowired
    public MetadataTagSetItemServiceImpl(MetadataTagSetItemMapper mapper, MetadataTagSetItemEntityMapper entityMapper) {
        super(mapper, entityMapper);
        childrenOperator = new DefaultChildrenOperator<>(mapper, entityMapper);
    }

    @Override
    public ChildrenOperator<MetadataTagSetItemDTO> childrenOperator() {
        return childrenOperator;
    }

}
