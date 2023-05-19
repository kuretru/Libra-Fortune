package com.kuretru.web.libra.service;

import com.kuretru.microservices.web.service.BaseService;
import com.kuretru.web.libra.entity.query.LedgerEntryTagQuery;
import com.kuretru.web.libra.entity.transfer.LedgerEntryTagDTO;

import java.util.List;
import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
public interface LedgerEntryTagService extends BaseService<LedgerEntryTagDTO, LedgerEntryTagQuery> {


    /**
     * 根据账本条目明细ID列表删除所有标签记录
     *
     * @param entryDetailIds 账本条目明细ID列表
     */
    void removeByEntryDetailIds(List<UUID> entryDetailIds);

}
