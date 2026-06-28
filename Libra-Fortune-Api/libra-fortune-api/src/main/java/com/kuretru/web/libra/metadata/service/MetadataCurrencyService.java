package com.kuretru.web.libra.metadata.service;

import com.kuretru.microservices.common.entity.enums.EnumDTO;
import com.kuretru.microservices.web.v2.entity.query.EmptyQuery;
import com.kuretru.microservices.web.v2.service.BaseService;
import com.kuretru.microservices.web.v2.service.SequencedService;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataCurrencyDTO;

import java.util.List;

public interface MetadataCurrencyService extends BaseService<MetadataCurrencyDTO, EmptyQuery>, SequencedService<MetadataCurrencyDTO> {

    /**
     * 转成枚举值
     *
     * @return 枚举值
     */
    List<EnumDTO<String>> enums();

}
