package com.kuretru.web.libra.ledger.service;

import com.kuretru.microservices.web.v2.service.BaseService;
import com.kuretru.microservices.web.v2.service.ChildrenCapable;
import com.kuretru.web.libra.ledger.entity.query.LedgerEntryTagQuery;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerEntryTagDTO;

public interface LedgerEntryTagService extends BaseService<LedgerEntryTagDTO, LedgerEntryTagQuery>,
        ChildrenCapable<LedgerEntryTagDTO, LedgerEntryTagQuery> {

}
