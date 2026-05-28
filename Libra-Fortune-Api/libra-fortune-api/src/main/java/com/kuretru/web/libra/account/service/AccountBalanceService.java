package com.kuretru.web.libra.account.service;

import com.kuretru.microservices.web.v2.service.BaseService;
import com.kuretru.web.libra.account.entity.query.AccountBalanceQuery;
import com.kuretru.web.libra.account.entity.transfer.AccountBalanceDTO;

public interface AccountBalanceService extends BaseService<AccountBalanceDTO, AccountBalanceQuery> {
}
