package com.kuretru.web.libra.account.service;

import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.v2.service.BaseSequencedService;
import com.kuretru.web.libra.account.entity.query.AccountQuery;
import com.kuretru.web.libra.account.entity.transfer.AccountDTO;

public interface AccountService extends BaseSequencedService<AccountDTO, AccountQuery> {

    /**
     *
     * 验证是否有权限访问账本
     *
     * @param id 账本ID
     * @throws ServiceException 无权限时返回异常
     */
    void verifyOwner(Long id) throws ServiceException;

}
