package com.kuretru.web.libra.metadata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.common.entity.enums.EnumDTO;
import com.kuretru.microservices.web.entity.query.EmptyQuery;
import com.kuretru.microservices.web.service.impl.BaseSequencedServiceImpl;
import com.kuretru.web.libra.metadata.entity.data.MetadataCurrencyDO;
import com.kuretru.web.libra.metadata.entity.mapper.MetadataCurrencyEntityMapper;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataCurrencyDTO;
import com.kuretru.web.libra.metadata.mapper.MetadataCurrencyMapper;
import com.kuretru.web.libra.metadata.service.MetadataCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MetadataCurrencyServiceImpl
        extends BaseSequencedServiceImpl<MetadataCurrencyMapper, MetadataCurrencyDO, MetadataCurrencyDTO, EmptyQuery>
        implements MetadataCurrencyService {

    @Autowired
    public MetadataCurrencyServiceImpl(MetadataCurrencyMapper mapper, MetadataCurrencyEntityMapper entityMapper) {
        super(mapper, entityMapper);
    }

    @Override
    public List<EnumDTO<String>> enums() {
        var records = list(null);
        var result = new ArrayList<EnumDTO<String>>();
        for (var record : records) {
            result.add(new EnumDTO<>(record.getCode(), "%s(%s)".formatted(record.getName(), record.getSymbol())));
        }
        return result;
    }

    @Override
    protected MetadataCurrencyDO findDuplicateRecord(MetadataCurrencyDTO record) {
        var queryWrapper = new QueryWrapper<MetadataCurrencyDO>();
        queryWrapper.eq("code", record.getCode());
        return mapper.selectOne(queryWrapper);
    }

}
