package com.kuretru.web.libra.account.service;

import com.kuretru.web.libra.account.entity.business.AccountBalanceResultBO;
import com.kuretru.web.libra.account.entity.query.AccountBalanceQuery;

public interface AccountBalanceService {

    AccountBalanceResultBO list(AccountBalanceQuery query);

}
