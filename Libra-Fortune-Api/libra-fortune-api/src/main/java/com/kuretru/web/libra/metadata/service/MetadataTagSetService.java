package com.kuretru.web.libra.metadata.service;

import com.kuretru.microservices.web.v2.service.BaseSequencedService;
import com.kuretru.web.libra.metadata.entity.query.MetadataTagSetQuery;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataTagSetDTO;

public interface MetadataTagSetService extends BaseSequencedService<MetadataTagSetDTO, MetadataTagSetQuery> {

}
