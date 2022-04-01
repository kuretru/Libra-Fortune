package com.kuretru.web.libra.service;

import com.kuretru.microservices.web.service.BaseService;
import com.kuretru.web.libra.entity.query.LedgerEntryQuery;
import com.kuretru.web.libra.entity.transfer.LedgerEntryDTO;

import java.util.UUID;


public interface LedgerEntryService extends BaseService<LedgerEntryDTO, LedgerEntryQuery> {

    Boolean existCategoryId(UUID uuid);

}
