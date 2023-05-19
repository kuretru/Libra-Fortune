package com.kuretru.web.libra.service;

import com.kuretru.microservices.web.service.BaseService;
import com.kuretru.web.libra.entity.query.LedgerCategoryQuery;
import com.kuretru.web.libra.entity.transfer.LedgerCategoryDTO;

import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
public interface LedgerCategoryService extends BaseService<LedgerCategoryDTO, LedgerCategoryQuery> {

    /**
     * 不鉴权，直接根据UUID查询分类
     *
     * @param uuid UUID
     * @return 账本实体
     */
    LedgerCategoryDTO getDirect(UUID uuid);

}
