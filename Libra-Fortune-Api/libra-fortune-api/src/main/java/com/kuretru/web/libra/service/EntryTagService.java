package com.kuretru.web.libra.service;

import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.BaseService;
import com.kuretru.web.libra.entity.query.EntryTagQuery;
import com.kuretru.web.libra.entity.transfer.EntryTagDTO;

import java.util.UUID;


public interface EntryTagService extends BaseService<EntryTagDTO, EntryTagQuery> {
    boolean getUserEntryPermission(UUID userId, UUID entryId) throws ServiceException.BadRequest;

    void deleteByTagId(UUID id);
}