package com.kuretru.web.libra.service;

import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.BaseService;
import com.kuretru.web.libra.entity.query.LedgerQuery;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;

import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
public interface LedgerService extends BaseService<LedgerDTO, LedgerQuery> {

    /**
     * 验证当前用户是否是指定账本的拥有者
     *
     * @param ledgerId 账本ID
     * @throws ServiceException 不是拥有者时引发业务异常
     */
    void verifyIamLedgerOwner(UUID ledgerId) throws ServiceException;

}
