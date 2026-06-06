package com.kuretru.web.libra.ledger.service;

import com.kuretru.microservices.web.v2.service.BaseSequencedService;
import com.kuretru.web.libra.ledger.entity.query.LedgerQuery;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerDTO;

public interface LedgerService extends BaseSequencedService<LedgerDTO, LedgerQuery> {

}
