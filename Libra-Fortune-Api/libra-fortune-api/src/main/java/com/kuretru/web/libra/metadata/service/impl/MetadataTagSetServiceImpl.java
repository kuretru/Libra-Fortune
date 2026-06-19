package com.kuretru.web.libra.metadata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.common.entity.enums.EnumDTO;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.v2.service.impl.BaseSequencedServiceImpl;
import com.kuretru.web.libra.metadata.entity.data.MetadataTagSetDO;
import com.kuretru.web.libra.metadata.entity.mapper.MetadataTagSetEntityMapper;
import com.kuretru.web.libra.metadata.entity.query.MetadataTagSetQuery;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataTagSetDTO;
import com.kuretru.web.libra.metadata.mapper.MetadataTagSetMapper;
import com.kuretru.web.libra.metadata.service.MetadataTagSetItemService;
import com.kuretru.web.libra.metadata.service.MetadataTagSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ListFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
public class MetadataTagSetServiceImpl extends BaseSequencedServiceImpl<MetadataTagSetMapper, MetadataTagSetDO, MetadataTagSetDTO, MetadataTagSetQuery> implements MetadataTagSetService {

    private final MetadataTagSetItemService itemService;

    @Autowired
    public MetadataTagSetServiceImpl(MetadataTagSetMapper mapper, MetadataTagSetEntityMapper entityMapper, MetadataTagSetItemService itemService) {
        super(mapper, entityMapper);
        this.itemService = itemService;
    }

    @Override
    protected QueryWrapper<MetadataTagSetDO> buildQueryWrapper(MetadataTagSetQuery query) {
        var result = super.buildQueryWrapper(query);
        if (StringUtils.hasText(query.getTagNameLike())) {

        }
        return result;
    }

    @Override
    public List<EnumDTO<Long>> enums() {
        var records = list(null);
        var result = new ArrayList<EnumDTO<Long>>();
        for (var record : records) {
            var item = new EnumDTO<>(record.getName(), record.getId());
            var children = new ArrayList<EnumDTO<Long>>();
            for (var child : record.getItems()) {
                children.add(new EnumDTO<>(child.getName(), child.getId()));
            }
            item.setChildren(children);
            result.add(item);
        }
        return result;
    }

    @Override
    public void verifyTagSetItems(List<Long> tagSetItemIdList) throws ServiceException {
        var tagSetItemIdSet = new HashSet<>(tagSetItemIdList);
        var tagSets = list(new MetadataTagSetQuery());
        for (var tagSet : tagSets) {
            var hasOne = false;
            var hasMultiple = false;
            for (var tagSetItem : tagSet.getItems()) {
                if (tagSetItemIdSet.contains(tagSetItem.getId())) {
                    if (hasOne) {
                        hasMultiple = true;
                    } else {
                        hasOne = true;
                    }
                    tagSetItemIdSet.remove(tagSetItem.getId());
                }
            }
            if (tagSet.getRequired() && !hasOne) {
                throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, String.format("[%s]标签为必选项但未选择", tagSet.getName()));
            } else if (!tagSet.getAllowMultiple() && hasMultiple) {
                throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, String.format("[%s]标签不允许多选", tagSet.getName()));
            }
        }
        if (!tagSetItemIdSet.isEmpty()) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, String.format("未知的标签[%s]", ListFormat.getInstance().format(new ArrayList<>(tagSetItemIdSet))));
        }
    }

    @Override
    protected MetadataTagSetDO findDuplicateRecord(MetadataTagSetDTO record) {
        var queryWrapper = new QueryWrapper<MetadataTagSetDO>();
        queryWrapper.eq("name", record.getName());
        return mapper.selectOne(queryWrapper);
    }

    @Override
    protected MetadataTagSetDTO afterGet(MetadataTagSetDO record) throws ServiceException {
        var result = super.afterGet(record);
        result.setItems(itemService.listByParentId(record.getId()));
        return result;
    }

    @Override
    protected List<MetadataTagSetDTO> afterList(MetadataTagSetQuery query, List<MetadataTagSetDO> records) throws ServiceException {
        var result = super.afterList(query, records);
        var idList = result.stream().map(MetadataTagSetDTO::getId).toList();
        if (idList.isEmpty()) {
            return result;
        }

        var itemsMap = itemService.listByParentId(idList);
        result.forEach(record -> record.setItems(itemsMap.getOrDefault(record.getId(), Collections.emptyList())));
        return result;
    }

}
