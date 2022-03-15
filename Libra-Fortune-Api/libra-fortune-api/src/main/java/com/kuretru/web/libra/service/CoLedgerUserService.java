package com.kuretru.web.libra.service;

import com.kuretru.api.common.service.BaseService;
import com.kuretru.web.libra.entity.data.CoLedgerUserDO;
import com.kuretru.web.libra.entity.query.CoLedgerUserQuery;
import com.kuretru.web.libra.entity.transfer.CoLedgerUserDTO;

import java.util.UUID;
import java.util.List;

public interface CoLedgerUserService extends BaseService<CoLedgerUserDTO, CoLedgerUserQuery> {
    Boolean getLedgerPermission(UUID ledgerId, UUID userId, boolean isWritable);
}