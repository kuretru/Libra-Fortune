package com.kuretru.web.libra.ledger.service.impl;

import com.kuretru.microservices.web.entity.query.EmptyQuery;
import com.kuretru.microservices.web.service.children.ChildrenOperator;
import com.kuretru.microservices.web.service.children.DefaultChildrenOperator;
import com.kuretru.microservices.web.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.ledger.entity.data.LedgerEntryDetailDO;
import com.kuretru.web.libra.ledger.entity.mapper.LedgerEntryDetailEntityMapper;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerEntryDetailDTO;
import com.kuretru.web.libra.ledger.mapper.LedgerEntryDetailMapper;
import com.kuretru.web.libra.ledger.service.LedgerEntryDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LedgerEntryDetailServiceImpl
        extends BaseServiceImpl<LedgerEntryDetailMapper, LedgerEntryDetailDO, LedgerEntryDetailDTO, EmptyQuery>
        implements LedgerEntryDetailService {

    private final ChildrenOperator<LedgerEntryDetailDTO, EmptyQuery> childrenOperator;

    @Autowired
    public LedgerEntryDetailServiceImpl(LedgerEntryDetailMapper mapper, LedgerEntryDetailEntityMapper entityMapper) {
        super(mapper, entityMapper);
        this.childrenOperator = new DefaultChildrenOperator<>(
                mapper, entityMapper,
                LedgerEntryDetailDO.class, LedgerEntryDetailDTO.class, EmptyQuery.class);
    }

    @Override
    public ChildrenOperator<LedgerEntryDetailDTO, EmptyQuery> childrenOperator() {
        return childrenOperator;
    }

}
