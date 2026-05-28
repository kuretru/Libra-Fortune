package com.kuretru.web.libra.metadata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.v2.service.impl.BaseSequencedServiceImpl;
import com.kuretru.web.libra.metadata.entity.data.MetadataTagSetDO;
import com.kuretru.web.libra.metadata.entity.mapper.MetadataTagSetEntityMapper;
import com.kuretru.web.libra.metadata.entity.query.MetadataTagSetItemQuery;
import com.kuretru.web.libra.metadata.entity.query.MetadataTagSetQuery;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataTagSetDTO;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataTagSetItemDTO;
import com.kuretru.web.libra.metadata.mapper.MetadataTagSetMapper;
import com.kuretru.web.libra.metadata.service.MetadataTagSetItemService;
import com.kuretru.web.libra.metadata.service.MetadataTagSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetadataTagSetServiceImpl
        extends BaseSequencedServiceImpl<MetadataTagSetMapper, MetadataTagSetDO, MetadataTagSetDTO, MetadataTagSetQuery>
        implements MetadataTagSetService {

    private final MetadataTagSetItemService itemService;

    @Autowired
    public MetadataTagSetServiceImpl(MetadataTagSetMapper mapper, MetadataTagSetEntityMapper entityMapper,
                                     MetadataTagSetItemService itemService) {
        super(mapper, entityMapper);
        this.itemService = itemService;
    }

    @Override
    protected MetadataTagSetDO findDuplicateRecord(MetadataTagSetDTO record) {
        var queryWrapper = new QueryWrapper<MetadataTagSetDO>();
        queryWrapper.eq("name", record.getName());
        return mapper.selectOne(queryWrapper);
    }

    @Override
    protected MetadataTagSetDTO afterGet(MetadataTagSetDO record) throws ServiceException {
        var itemQuery = new MetadataTagSetItemQuery();
        itemQuery.setSetId(record.getId());
        var result = super.afterGet(record);
        result.setItems(itemService.list(itemQuery));
        return result;
    }

    @Override
    protected List<MetadataTagSetDTO> afterList(MetadataTagSetQuery query, List<MetadataTagSetDO> records) throws ServiceException {
        var result = super.afterList(query, records);
        var idList = result.stream().map(MetadataTagSetDTO::getId).toList();
        if (idList.isEmpty()) {
            return result;
        }

        var itemQuery = new MetadataTagSetItemQuery();
        itemQuery.setSetIdIn(idList);
        var items = itemService.list(itemQuery);
        var itemsMap = items.stream().collect(Collectors.groupingBy(MetadataTagSetItemDTO::getSetId));
        result.forEach(record -> record.setItems(itemsMap.getOrDefault(record.getId(), Collections.emptyList())));
        return result;
    }

}
