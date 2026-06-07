package com.kuretru.web.libra.ledger.service.impl;

import com.kuretru.microservices.web.v2.service.impl.BaseInnerChildServiceImpl;
import com.kuretru.web.libra.ledger.entity.data.LedgerEntryTagDO;
import com.kuretru.web.libra.ledger.entity.mapper.LedgerEntryTagEntityMapper;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerEntryTagDTO;
import com.kuretru.web.libra.ledger.mapper.LedgerEntryTagMapper;
import com.kuretru.web.libra.ledger.service.LedgerEntryTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LedgerEntryTagServiceImpl
        extends BaseInnerChildServiceImpl<LedgerEntryTagMapper, LedgerEntryTagDO, LedgerEntryTagDTO>
        implements LedgerEntryTagService {

    @Autowired
    public LedgerEntryTagServiceImpl(LedgerEntryTagMapper mapper, LedgerEntryTagEntityMapper entityMapper) {
        super(mapper, entityMapper);
    }

    @Override
    protected String getParentIdColumn() {
        return "entry_id";
    }

    @Override
    protected Long getParentId(LedgerEntryTagDTO record) {
        return record.getEntryId();
    }

    @Override
    protected void setParentId(Long parentId, LedgerEntryTagDTO record) {
        record.setEntryId(parentId);
    }

    @Override
    protected boolean bizEqual(LedgerEntryTagDO oldRecord, LedgerEntryTagDTO newRecord) {
        return oldRecord.getTagId().equals(newRecord.getTagId());
    }

}
