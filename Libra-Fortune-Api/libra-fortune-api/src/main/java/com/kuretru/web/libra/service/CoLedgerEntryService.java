package com.kuretru.web.libra.service;

import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.BaseService;
import com.kuretru.web.libra.entity.query.CoLedgerEntryQuery;
import com.kuretru.web.libra.entity.transfer.CoLedgerEntryDTO;

import java.util.UUID;

public interface CoLedgerEntryService extends BaseService<CoLedgerEntryDTO, CoLedgerEntryQuery> {

    Boolean getCoLedgerEntryExist(UUID userId, UUID entryId);

    CoLedgerEntryDTO save(String ledgerId, CoLedgerEntryDTO record) throws ServiceException;

    void deleteByEntryId(UUID uuid);

}
