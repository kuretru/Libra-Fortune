package com.kuretru.web.libra.controller;

import com.kuretru.microservices.authentication.annotaion.RequireAuthorization;
import com.kuretru.microservices.authentication.context.AccessTokenContext;
import com.kuretru.microservices.web.constant.EmptyConstants;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.controller.BaseRestController;
import com.kuretru.microservices.web.entity.ApiResponse;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.web.libra.entity.query.LedgerQuery;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.service.LedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/ledgers")
@RequireAuthorization
public class LedgerController extends BaseRestController<LedgerService, LedgerDTO, LedgerQuery> {

    @Autowired
    public LedgerController(LedgerService service) {
        super(service);
    }

    @Override
    public ApiResponse<LedgerDTO> get(UUID id) throws ServiceException {
        UUID userId = AccessTokenContext.getUserId();
        if (id == null || EmptyConstants.EMPTY_UUID.equals(id)) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定ID或ID错误");
        }
        LedgerDTO result = service.get(id, userId);
        if (null == result) {
            // 指定ID查询单个实体但实体不存在时，认为是用户方ID输入错误，因此抛异常
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "指定资源不存在");
        }
        return ApiResponse.success(result);
    }

}
