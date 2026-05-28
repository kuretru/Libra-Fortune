package com.kuretru.web.libra.metadata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.web.v2.service.impl.BaseSequencedServiceImpl;
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

    @Autowired
    public MetadataTagSetItemServiceImpl(MetadataTagSetItemMapper mapper, MetadataTagSetItemEntityMapper entityMapper) {
        super(mapper, entityMapper);
    }

    @Override
    protected MetadataTagSetItemDO findDuplicateRecord(MetadataTagSetItemDTO record) {
        var queryWrapper = new QueryWrapper<MetadataTagSetItemDO>();
        queryWrapper.eq("set_id", record.getSetId());
        queryWrapper.eq("name", record.getName());
        return mapper.selectOne(queryWrapper);
    }

}
