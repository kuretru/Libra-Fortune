package com.kuretru.web.libra.metadata.service.impl;

import com.kuretru.microservices.web.v2.entity.query.EmptyQuery;
import com.kuretru.microservices.web.v2.service.impl.BaseSequencedServiceImpl;
import com.kuretru.web.libra.metadata.entity.data.MetadataCurrencyDO;
import com.kuretru.web.libra.metadata.entity.mapper.MetadataCurrencyEntityMapper;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataCurrencyDTO;
import com.kuretru.web.libra.metadata.mapper.MetadataCurrencyMapper;
import com.kuretru.web.libra.metadata.service.MetadataCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetadataCurrencyServiceImpl
        extends BaseSequencedServiceImpl<MetadataCurrencyMapper, MetadataCurrencyDO, MetadataCurrencyDTO, EmptyQuery>
        implements MetadataCurrencyService {

    @Autowired
    public MetadataCurrencyServiceImpl(MetadataCurrencyMapper mapper, MetadataCurrencyEntityMapper entityMapper) {
        super(mapper, entityMapper);
    }

}
