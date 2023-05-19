package com.kuretru.web.libra.service;

import com.kuretru.microservices.web.service.BaseService;
import com.kuretru.web.libra.entity.query.LedgerTagQuery;
import com.kuretru.web.libra.entity.transfer.LedgerTagDTO;

import java.util.Map;
import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
public interface LedgerTagService extends BaseService<LedgerTagDTO, LedgerTagQuery> {

    /**
     * 列出我的账本标签
     *
     * @return 账本标签Map
     */
    Map<UUID, LedgerTagDTO> listMyLedgerTags();

}
