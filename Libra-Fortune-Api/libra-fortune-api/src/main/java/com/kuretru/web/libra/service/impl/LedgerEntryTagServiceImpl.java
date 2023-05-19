package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.web.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerEntryTagDO;
import com.kuretru.web.libra.entity.mapper.LedgerEntryTagEntityMapper;
import com.kuretru.web.libra.entity.query.LedgerEntryTagQuery;
import com.kuretru.web.libra.entity.transfer.LedgerEntryTagDTO;
import com.kuretru.web.libra.mapper.LedgerEntryTagMapper;
import com.kuretru.web.libra.service.LedgerEntryTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Service
public class LedgerEntryTagServiceImpl
        extends BaseServiceImpl<LedgerEntryTagMapper, LedgerEntryTagDO, LedgerEntryTagDTO, LedgerEntryTagQuery>
        implements LedgerEntryTagService {

    @Autowired
    public LedgerEntryTagServiceImpl(LedgerEntryTagMapper mapper, LedgerEntryTagEntityMapper entityMapper) {
        super(mapper, entityMapper);
    }

    @Override
    public void removeByEntryDetailIds(List<UUID> entryDetailIds) {
        QueryWrapper<LedgerEntryTagDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("entry_detail_id", entryDetailIds);
        mapper.delete(queryWrapper);
    }

}
