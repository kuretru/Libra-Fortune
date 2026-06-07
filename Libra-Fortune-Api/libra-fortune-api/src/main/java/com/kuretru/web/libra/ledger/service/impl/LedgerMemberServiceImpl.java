package com.kuretru.web.libra.ledger.service.impl;

import com.kuretru.microservices.web.v2.service.impl.BaseInnerChildServiceImpl;
import com.kuretru.web.libra.ledger.entity.data.LedgerMemberDO;
import com.kuretru.web.libra.ledger.entity.mapper.LedgerMemberEntityMapper;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerMemberDTO;
import com.kuretru.web.libra.ledger.mapper.LedgerMemberMapper;
import com.kuretru.web.libra.ledger.service.LedgerMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ledgerMemberV2Service")
public class LedgerMemberServiceImpl extends BaseInnerChildServiceImpl<LedgerMemberMapper, LedgerMemberDO, LedgerMemberDTO>
        implements LedgerMemberService {

    @Autowired
    public LedgerMemberServiceImpl(LedgerMemberMapper mapper, LedgerMemberEntityMapper entityMapper) {
        super(mapper, entityMapper);
    }

    @Override
    protected String getParentIdColumn() {
        return "ledger_id";
    }

    @Override
    protected Long getParentId(LedgerMemberDTO record) {
        return record.getLedgerId();
    }

    @Override
    protected void setParentId(Long parentId, LedgerMemberDTO record) {
        record.setLedgerId(parentId);
    }

    @Override
    protected boolean bizEqual(LedgerMemberDO oldRecord, LedgerMemberDTO newRecord) {
        return oldRecord.getUsername().equals(newRecord.getUsername())
                && oldRecord.getDefaultFundedRatio().equals(newRecord.getDefaultFundedRatio());
    }

}
