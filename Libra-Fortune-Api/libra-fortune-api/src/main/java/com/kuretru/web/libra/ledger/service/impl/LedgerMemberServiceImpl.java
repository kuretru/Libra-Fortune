package com.kuretru.web.libra.ledger.service.impl;

import com.kuretru.microservices.web.entity.query.EmptyQuery;
import com.kuretru.microservices.web.service.children.ChildrenOperator;
import com.kuretru.microservices.web.service.children.DefaultChildrenOperator;
import com.kuretru.microservices.web.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.ledger.entity.data.LedgerMemberDO;
import com.kuretru.web.libra.ledger.entity.mapper.LedgerMemberEntityMapper;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerMemberDTO;
import com.kuretru.web.libra.ledger.mapper.LedgerMemberMapper;
import com.kuretru.web.libra.ledger.service.LedgerMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LedgerMemberServiceImpl extends BaseServiceImpl<LedgerMemberMapper, LedgerMemberDO, LedgerMemberDTO, EmptyQuery>
        implements LedgerMemberService {

    private final ChildrenOperator<LedgerMemberDTO, EmptyQuery> childrenOperator;

    @Autowired
    public LedgerMemberServiceImpl(LedgerMemberMapper mapper, LedgerMemberEntityMapper entityMapper) {
        super(mapper, entityMapper);
        this.childrenOperator = new DefaultChildrenOperator<>(
                mapper, entityMapper,
                LedgerMemberDO.class, LedgerMemberDTO.class, EmptyQuery.class);
    }

    @Override
    public ChildrenOperator<LedgerMemberDTO, EmptyQuery> childrenOperator() {
        return childrenOperator;
    }

}
