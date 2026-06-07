package com.kuretru.web.libra.metadata.service;

import com.kuretru.microservices.common.entity.enums.EnumDTO;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.v2.service.BaseSequencedService;
import com.kuretru.web.libra.metadata.entity.query.MetadataTagSetQuery;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataTagSetDTO;

import java.util.List;

public interface MetadataTagSetService extends BaseSequencedService<MetadataTagSetDTO, MetadataTagSetQuery> {

    /**
     * 转成枚举值
     *
     * @return 枚举值
     */
    List<EnumDTO<Long>> enums();

    /**
     * 验证标签项
     *
     * @param tagSetItemIdList 标签项ID列表
     * @throws ServiceException 验证失败时会返回ServiceException
     */
    void verifyTagSetItems(List<Long> tagSetItemIdList) throws ServiceException;

}
