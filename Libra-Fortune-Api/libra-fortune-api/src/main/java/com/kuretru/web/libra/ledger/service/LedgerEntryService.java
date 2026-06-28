package com.kuretru.web.libra.ledger.service;

import com.kuretru.microservices.web.v2.service.BaseService;
import com.kuretru.web.libra.ledger.entity.query.LedgerEntryQuery;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerEntryDTO;

public interface LedgerEntryService extends BaseService<LedgerEntryDTO, LedgerEntryQuery> {
}
