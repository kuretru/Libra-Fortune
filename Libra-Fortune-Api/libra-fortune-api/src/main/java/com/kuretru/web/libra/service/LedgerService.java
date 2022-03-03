package com.kuretru.web.libra.service;

import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.BaseService;
import com.kuretru.web.libra.entity.query.LedgerQuery;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;

import java.util.List;
import java.util.UUID;


public interface LedgerService extends BaseService<LedgerDTO, LedgerQuery> {

    /**
     * 查询指定用户的所有可用账本
     *
     * @param userId 指定用户ID
     * @return 所有可用账本
     * @throws ServiceException 业务异常
     */
    List<LedgerDTO> list(UUID userId) throws ServiceException;

}
