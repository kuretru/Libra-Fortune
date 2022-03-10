package com.kuretru.web.libra.service;

import com.kuretru.api.common.service.BaseService;
import com.kuretru.web.libra.entity.query.LedgerQuery;
import com.kuretru.web.libra.entity.query.SystemUserQuery;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.entity.transfer.SystemUserDTO;


public interface LedgerService extends BaseService<LedgerDTO, LedgerQuery> {

}