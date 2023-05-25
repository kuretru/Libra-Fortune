package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerEntryDetailTagDO;
import com.kuretru.web.libra.entity.mapper.LedgerEntryDetailTagEntityMapper;
import com.kuretru.web.libra.entity.query.LedgerEntryDetailTagQuery;
import com.kuretru.web.libra.entity.transfer.LedgerEntryDetailTagDTO;
import com.kuretru.web.libra.entity.transfer.LedgerTagDTO;
import com.kuretru.web.libra.mapper.LedgerEntryDetailTagMapper;
import com.kuretru.web.libra.service.LedgerEntryDetailTagService;
import com.kuretru.web.libra.service.LedgerTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Service
public class LedgerEntryDetailTagServiceImpl
        extends BaseServiceImpl<LedgerEntryDetailTagMapper, LedgerEntryDetailTagDO, LedgerEntryDetailTagDTO, LedgerEntryDetailTagQuery>
        implements LedgerEntryDetailTagService {

    private final LedgerTagService tagService;

    @Autowired
    public LedgerEntryDetailTagServiceImpl(LedgerEntryDetailTagMapper mapper, LedgerEntryDetailTagEntityMapper entityMapper,
                                           @Lazy LedgerTagService tagService) {
        super(mapper, entityMapper);
        this.tagService = tagService;
    }

    @Override
    public List<UUID> listByEntryDetailId(UUID entryDetailId) {
        QueryWrapper<LedgerEntryDetailTagDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("entry_detail_id", entryDetailId.toString());
        queryWrapper.orderByAsc("id");
        List<LedgerEntryDetailTagDTO> records = list(queryWrapper);
        List<UUID> result = Lists.newArrayListWithCapacity(records.size());
        records.forEach(record -> result.add(record.getTagId()));
        return result;
    }

    private void saveList(UUID entryDetailId, List<UUID> records) {
        records.forEach(record -> {
            LedgerEntryDetailTagDTO dto = new LedgerEntryDetailTagDTO();
            dto.setEntryDetailId(entryDetailId);
            dto.setTagId(record);
            super.save(dto);
        });
    }

    @Override
    public List<UUID> save(UUID entryDetailId, List<UUID> records) throws ServiceException {
        Map<UUID, LedgerTagDTO> myLedgerTag = tagService.listMyLedgerTagsMap();
        records.forEach(record -> {
            if (!myLedgerTag.containsKey(record)) {
                throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "非本人标签");
            }
        });
        saveList(entryDetailId, records);
        return listByEntryDetailId(entryDetailId);
    }

    @Override
    public List<UUID> update(UUID entryDetailId, List<UUID> records) throws ServiceException {
        Map<UUID, LedgerTagDTO> myLedgerTag = tagService.listMyLedgerTagsMap();
        List<UUID> oldRecords = listByEntryDetailId(entryDetailId);
        Set<UUID> oldRecordsSet = Sets.newHashSet(oldRecords);

        List<UUID> addList = Lists.newArrayListWithCapacity(records.size());
        records.forEach(record -> {
            if (oldRecordsSet.contains(record)) {
                oldRecordsSet.remove(record);
            } else {
                if (!myLedgerTag.containsKey(record)) {
                    throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "非本人标签");
                }
                addList.add(record);
            }
        });
        List<UUID> removeList = Lists.newArrayList(oldRecordsSet);

        if (addList.size() > 0) {
            saveList(entryDetailId, addList);
        }
        if (removeList.size() > 0) {
            removeList(removeList);
        }

        return listByEntryDetailId(entryDetailId);
    }

    private void removeList(List<UUID> records) {
        List<String> ids = records.stream().map(UUID::toString).toList();
        QueryWrapper<LedgerEntryDetailTagDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("tag_id", ids);
        mapper.delete(queryWrapper);
    }

    @Override
    public void removeByEntryDetailIds(List<UUID> entryDetailIds) {
        QueryWrapper<LedgerEntryDetailTagDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("entry_detail_id", entryDetailIds);
        mapper.delete(queryWrapper);
    }

}
