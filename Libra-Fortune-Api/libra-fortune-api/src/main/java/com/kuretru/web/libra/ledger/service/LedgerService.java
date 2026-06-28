package com.kuretru.web.libra.ledger.service;

import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.BaseService;
import com.kuretru.microservices.web.service.SequencedService;
import com.kuretru.web.libra.ledger.entity.query.LedgerQuery;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerDTO;

public interface LedgerService extends BaseService<LedgerDTO, LedgerQuery>, SequencedService<LedgerDTO> {

    void verifyCanManagerEntry(Long ledgerId) throws ServiceException;

}
