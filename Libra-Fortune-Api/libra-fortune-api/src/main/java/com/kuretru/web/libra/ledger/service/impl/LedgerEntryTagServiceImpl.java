package com.kuretru.web.libra.ledger.service.impl;

import com.kuretru.microservices.web.v2.service.ability.children.ChildrenOperator;
import com.kuretru.microservices.web.v2.service.ability.children.DefaultChildrenOperator;
import com.kuretru.microservices.web.v2.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.ledger.entity.data.LedgerEntryTagDO;
import com.kuretru.web.libra.ledger.entity.mapper.LedgerEntryTagEntityMapper;
import com.kuretru.web.libra.ledger.entity.query.LedgerEntryTagQuery;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerEntryTagDTO;
import com.kuretru.web.libra.ledger.mapper.LedgerEntryTagMapper;
import com.kuretru.web.libra.ledger.service.LedgerEntryTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LedgerEntryTagServiceImpl
        extends BaseServiceImpl<LedgerEntryTagMapper, LedgerEntryTagDO, LedgerEntryTagDTO, LedgerEntryTagQuery>
        implements LedgerEntryTagService {

    private final ChildrenOperator<LedgerEntryTagDTO, LedgerEntryTagQuery> childrenOperator;

    @Autowired
    public LedgerEntryTagServiceImpl(LedgerEntryTagMapper mapper, LedgerEntryTagEntityMapper entityMapper) {
        super(mapper, entityMapper);
        this.childrenOperator = new DefaultChildrenOperator<>(
                mapper, entityMapper,
                LedgerEntryTagDO.class, LedgerEntryTagDTO.class, LedgerEntryTagQuery.class);
    }


    @Override
    public ChildrenOperator<LedgerEntryTagDTO, LedgerEntryTagQuery> childrenOperator() {
        return childrenOperator;
    }

}
