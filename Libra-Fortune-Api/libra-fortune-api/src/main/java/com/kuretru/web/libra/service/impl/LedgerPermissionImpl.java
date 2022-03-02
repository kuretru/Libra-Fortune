package com.kuretru.web.libra.service.impl;

import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.entity.ApiResponse;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerDO;
import com.kuretru.web.libra.entity.data.LedgerPermissionDO;
import com.kuretru.web.libra.entity.query.LedgerPermissionQuery;
import com.kuretru.web.libra.entity.query.LedgerQuery;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.entity.transfer.LedgerPermissionDTO;
import com.kuretru.web.libra.mapper.LedgerMapper;
import com.kuretru.web.libra.mapper.LedgerPermissionMapper;
import com.kuretru.web.libra.service.LedgerPermissionService;
import com.kuretru.web.libra.service.LedgerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Service
public class LedgerPermissionImpl extends BaseServiceImpl<LedgerPermissionMapper, LedgerPermissionDO, LedgerPermissionDTO, LedgerPermissionQuery> implements LedgerPermissionService {

    @Autowired
    public LedgerPermissionImpl(LedgerPermissionMapper mapper) {
        super(mapper, LedgerPermissionDO.class, LedgerPermissionDTO.class, LedgerPermissionQuery.class);
    }


}
