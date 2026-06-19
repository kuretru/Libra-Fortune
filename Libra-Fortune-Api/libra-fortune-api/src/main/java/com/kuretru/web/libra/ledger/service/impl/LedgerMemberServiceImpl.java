package com.kuretru.web.libra.ledger.service.impl;

import com.kuretru.microservices.web.v2.entity.query.EmptyQuery;
import com.kuretru.microservices.web.v2.service.ability.children.ChildrenOperator;
import com.kuretru.microservices.web.v2.service.ability.children.DefaultChildrenOperator;
import com.kuretru.microservices.web.v2.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.ledger.entity.data.LedgerMemberDO;
import com.kuretru.web.libra.ledger.entity.mapper.LedgerMemberEntityMapper;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerMemberDTO;
import com.kuretru.web.libra.ledger.mapper.LedgerMemberMapper;
import com.kuretru.web.libra.ledger.service.LedgerMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ledgerMemberV2Service")
public class LedgerMemberServiceImpl extends BaseServiceImpl<LedgerMemberMapper, LedgerMemberDO, LedgerMemberDTO, EmptyQuery>
        implements LedgerMemberService {

    private final ChildrenOperator<LedgerMemberDTO> childrenOperator;

    @Autowired
    public LedgerMemberServiceImpl(LedgerMemberMapper mapper, LedgerMemberEntityMapper entityMapper) {
        super(mapper, entityMapper);
        this.childrenOperator = new DefaultChildrenOperator<>(mapper, entityMapper);
    }

    @Override
    public ChildrenOperator<LedgerMemberDTO> childrenOperator() {
        return childrenOperator;
    }

}
