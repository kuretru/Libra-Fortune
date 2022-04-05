package com.kuretru.web.libra.service;

import com.kuretru.microservices.web.service.BaseService;
import com.kuretru.web.libra.entity.query.LedgerCategoryQuery;
import com.kuretru.web.libra.entity.transfer.LedgerCategoryDTO;

import java.util.UUID;


public interface LedgerCategoryService extends BaseService<LedgerCategoryDTO, LedgerCategoryQuery> {

    LedgerCategoryDTO get(UUID id, UUID userId);

}
