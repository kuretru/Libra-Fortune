package com.kuretru.web.libra.service;

import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.BaseService;
import com.kuretru.web.libra.entity.query.LedgerQuery;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;

import java.util.List;
import java.util.UUID;


public interface LedgerService extends BaseService<LedgerDTO, LedgerQuery> {

}
