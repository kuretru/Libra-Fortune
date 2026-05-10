package com.kuretru.web.libra.metadata.service.impl;

import com.kuretru.microservices.common.entity.enums.EnumDTO;
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
import java.util.List;

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

}
