package com.kuretru.web.libra.ledger.service.impl;

import com.kuretru.microservices.web.v2.service.impl.BaseOneToManyServiceImpl;
import com.kuretru.web.libra.ledger.entity.data.LedgerEntryDetailDO;
import com.kuretru.web.libra.ledger.entity.mapper.LedgerEntryDetailEntityMapper;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerEntryDetailDTO;
import com.kuretru.web.libra.ledger.mapper.LedgerEntryDetailMapper;
import com.kuretru.web.libra.ledger.service.LedgerEntryDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("ledgerV2EntryDetailService")
public class LedgerEntryDetailServiceImpl
        extends BaseOneToManyServiceImpl<LedgerEntryDetailMapper, LedgerEntryDetailDO, LedgerEntryDetailDTO>
        implements LedgerEntryDetailService {

    @Autowired
    public LedgerEntryDetailServiceImpl(LedgerEntryDetailMapper mapper, LedgerEntryDetailEntityMapper entityMapper) {
        super(mapper, entityMapper);
    }

    @Override
    protected String getParentIdColumn() {
        return "entry_id";
    }

    @Override
    protected Long getParentId(LedgerEntryDetailDTO record) {
        return record.getEntryId();
    }

    @Override
    protected void setParentId(Long parentId, LedgerEntryDetailDTO record) {
        record.setEntryId(parentId);
    }

    @Override
    protected boolean bizEqual(LedgerEntryDetailDO oldRecord, LedgerEntryDetailDTO newRecord) {
        return oldRecord.getUsername().equals(newRecord.getUsername())
                && oldRecord.getAccountId().equals(newRecord.getAccountId())
                && Objects.equals(oldRecord.getFundedRatio(), newRecord.getFundedRatio())
                && Objects.equals(oldRecord.getAmount(), newRecord.getAmount());
    }

}
