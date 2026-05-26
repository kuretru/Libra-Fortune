package com.kuretru.web.libra.metadata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.common.entity.enums.EnumDTO;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.v2.entity.query.EmptyQuery;
import com.kuretru.microservices.web.v2.service.impl.BaseSequencedServiceImpl;
import com.kuretru.web.libra.metadata.entity.data.MetadataCategoryDO;
import com.kuretru.web.libra.metadata.entity.mapper.MetadataCategoryEntityMapper;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataCategoryDTO;
import com.kuretru.web.libra.metadata.mapper.MetadataCategoryMapper;
import com.kuretru.web.libra.metadata.service.MetadataCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetadataCategoryServiceImpl
        extends BaseSequencedServiceImpl<MetadataCategoryMapper, MetadataCategoryDO, MetadataCategoryDTO, EmptyQuery>
        implements MetadataCategoryService {

    @Autowired
    public MetadataCategoryServiceImpl(MetadataCategoryMapper mapper, MetadataCategoryEntityMapper entityMapper) {
        super(mapper, entityMapper);
    }

    @Override
    public List<EnumDTO> enums() {
        var records = list(null);
        var result = new ArrayList<EnumDTO>();
        for (var record : records) {
            result.add(new EnumDTO(record.getName(), String.valueOf(record.getId())));
        }
        return result;
    }

    @Override
    public int getMaxSequence(MetadataCategoryDTO record) {
        var queryWrapper = new QueryWrapper<MetadataCategoryDO>();
        if (record.getParentId() == null) {
            queryWrapper.isNull("parent_id");
        } else {
            queryWrapper.eq("parent_id", record.getParentId());
        }
        Integer result = mapper.getMaxSequence(queryWrapper);
        return null == result ? 0 : result;
    }

    @Override
    protected MetadataCategoryDO findDuplicateRecord(MetadataCategoryDTO record) {
        var queryWrapper = new QueryWrapper<MetadataCategoryDO>();
        queryWrapper.eq("name", record.getName());
        if (record.getParentId() == null) {
            queryWrapper.isNull("parent_id");
        } else {
            queryWrapper.eq("parent_id", record.getParentId());
        }
        return mapper.selectOne(queryWrapper);
    }

    @Override
    protected MetadataCategoryDTO afterGet(MetadataCategoryDO record) throws ServiceException {
        var queryWrapper = new QueryWrapper<MetadataCategoryDO>();
        queryWrapper.eq("parent_id", record.getId());
        var children = mapper.selectList(queryWrapper);
        var result = super.afterGet(record);
        result.setChildren(entityMapper.doToDto(children));
        return result;
    }

    @Override
    protected QueryWrapper<MetadataCategoryDO> beforeList(EmptyQuery query) throws ServiceException {
        var queryWrapper = super.beforeList(query);
        queryWrapper.isNull("parent_id");
        return queryWrapper;
    }

    @Override
    protected List<MetadataCategoryDTO> afterList(EmptyQuery query, List<MetadataCategoryDO> records) throws ServiceException {
        var result = super.afterList(query, records);
        var idList = result.stream().map(MetadataCategoryDTO::getId).toList();
        if (idList.isEmpty()) {
            return result;
        }

        var queryWrapper = new QueryWrapper<MetadataCategoryDO>();
        queryWrapper.in("parent_id", idList);
        var children = mapper.selectList(queryWrapper);
        var childrenMap = entityMapper.doToDto(children).stream().collect(Collectors.groupingBy(MetadataCategoryDTO::getParentId));
        result.forEach(record -> record.setChildren(childrenMap.getOrDefault(String.valueOf(record.getId()), Collections.emptyList())));
        return result;
    }

}
