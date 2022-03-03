package com.kuretru.web.libra.service.impl;

import com.kuretru.api.common.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerPermissionDO;
import com.kuretru.web.libra.entity.query.LedgerPermissionQuery;
import com.kuretru.web.libra.entity.transfer.LedgerPermissionDTO;
import com.kuretru.web.libra.mapper.LedgerPermissionMapper;
import com.kuretru.web.libra.service.LedgerPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LedgerPermissionImpl extends BaseServiceImpl<LedgerPermissionMapper, LedgerPermissionDO, LedgerPermissionDTO, LedgerPermissionQuery> implements LedgerPermissionService {

    @Autowired
    public LedgerPermissionImpl(LedgerPermissionMapper mapper) {
        super(mapper, LedgerPermissionDO.class, LedgerPermissionDTO.class, LedgerPermissionQuery.class);
    }

}
