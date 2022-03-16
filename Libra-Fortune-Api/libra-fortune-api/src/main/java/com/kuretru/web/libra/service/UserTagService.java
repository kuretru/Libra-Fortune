package com.kuretru.web.libra.service;

import com.kuretru.api.common.service.BaseService;
import com.kuretru.web.libra.entity.query.UserTagQuery;
import com.kuretru.web.libra.entity.transfer.UserTagDTO;

import java.util.UUID;


public interface UserTagService extends BaseService<UserTagDTO, UserTagQuery> {

    Boolean userExistTag(UUID userId, UUID tagId);
}