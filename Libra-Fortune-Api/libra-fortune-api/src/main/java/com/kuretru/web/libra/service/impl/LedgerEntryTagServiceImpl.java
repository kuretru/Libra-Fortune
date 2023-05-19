package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerEntryTagDO;
import com.kuretru.web.libra.entity.mapper.LedgerEntryTagEntityMapper;
import com.kuretru.web.libra.entity.query.LedgerEntryTagQuery;
import com.kuretru.web.libra.entity.transfer.LedgerEntryTagDTO;
import com.kuretru.web.libra.entity.transfer.LedgerTagDTO;
import com.kuretru.web.libra.mapper.LedgerEntryTagMapper;
import com.kuretru.web.libra.service.LedgerEntryTagService;
import com.kuretru.web.libra.service.LedgerTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Service
public class LedgerEntryTagServiceImpl
        extends BaseServiceImpl<LedgerEntryTagMapper, LedgerEntryTagDO, LedgerEntryTagDTO, LedgerEntryTagQuery>
        implements LedgerEntryTagService {

    private final LedgerTagService tagService;

    @Autowired
    public LedgerEntryTagServiceImpl(LedgerEntryTagMapper mapper, LedgerEntryTagEntityMapper entityMapper,
                                     @Lazy LedgerTagService tagService) {
        super(mapper, entityMapper);
        this.tagService = tagService;
    }

    @Override
    public List<LedgerEntryTagDTO> listByEntryDetailId(UUID entryDetailId) {
        QueryWrapper<LedgerEntryTagDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("entry_detail_id", entryDetailId.toString());
        queryWrapper.orderByAsc("id");
        return list(queryWrapper);
    }

    @Override
    public List<LedgerEntryTagDTO> save(List<LedgerEntryTagDTO> records) throws ServiceException {
        Map<UUID, LedgerTagDTO> myLedgerTag = tagService.listMyLedgerTags();
        List<LedgerEntryTagDTO> result = new ArrayList<>(records.size());
        records.forEach(record -> {
            if (!myLedgerTag.containsKey(record.getTagId())) {
                throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "非本人标签");
            }
            result.add(super.save(record));
        });
        return result;
    }

    @Override
    public void removeByEntryDetailIds(List<UUID> entryDetailIds) {
        QueryWrapper<LedgerEntryTagDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("entry_detail_id", entryDetailIds);
        mapper.delete(queryWrapper);
    }

}
