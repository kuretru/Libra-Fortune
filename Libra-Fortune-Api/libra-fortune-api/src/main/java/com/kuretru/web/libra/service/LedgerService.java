package com.kuretru.web.libra.service;

import com.kuretru.microservices.web.service.BaseService;
import com.kuretru.web.libra.entity.query.LedgerQuery;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;

import java.util.UUID;


public interface LedgerService extends BaseService<LedgerDTO, LedgerQuery> {

    /**
     * 根据业务逻辑主键和用户ID查询一条记录
     *
     * @param uuid   业务逻辑主键UUID
     * @param userId 用户ID
     * @return 一条记录，找不到时返回Null
     */
    LedgerDTO get(UUID uuid, UUID userId);

}
