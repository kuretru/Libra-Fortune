package com.kuretru.web.libra.service;

import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.BaseService;
import com.kuretru.web.libra.entity.query.EntryTagQuery;
import com.kuretru.web.libra.entity.transfer.EntryTagDTO;

import java.util.UUID;


public interface EntryTagService extends BaseService<EntryTagDTO, EntryTagQuery> {

    boolean getUserEntryPermission(UUID userId, UUID entryId) throws ServiceException;

    void deleteByTagId(UUID id);

}
