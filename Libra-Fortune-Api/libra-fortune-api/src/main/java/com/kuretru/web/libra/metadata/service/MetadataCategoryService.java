package com.kuretru.web.libra.metadata.service;

import com.kuretru.microservices.common.entity.enums.EnumDTO;
import com.kuretru.microservices.web.v2.entity.query.EmptyQuery;
import com.kuretru.microservices.web.v2.service.BaseSequencedService;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataCategoryDTO;

import java.util.List;

public interface MetadataCategoryService extends BaseSequencedService<MetadataCategoryDTO, EmptyQuery> {

    /**
     * 转成枚举值
     *
     * @return 枚举值
     */
    List<EnumDTO> enums();

}
