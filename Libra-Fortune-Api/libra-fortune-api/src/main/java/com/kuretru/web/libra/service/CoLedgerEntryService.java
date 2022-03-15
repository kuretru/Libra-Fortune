package com.kuretru.web.libra.service;

import com.kuretru.api.common.service.BaseService;
import com.kuretru.web.libra.entity.query.CoLedgerEntryQuery;
import com.kuretru.web.libra.entity.query.CoLedgerUserQuery;
import com.kuretru.web.libra.entity.transfer.CoLedgerEntryDTO;
import com.kuretru.web.libra.entity.transfer.CoLedgerUserDTO;

import java.util.UUID;

public interface CoLedgerEntryService extends BaseService<CoLedgerEntryDTO, CoLedgerEntryQuery> {
}