package com.kuretru.web.libra.controller;

import com.kuretru.microservices.authentication.annotaion.RequireAuthorization;
import com.kuretru.microservices.authentication.context.AccessTokenContext;
import com.kuretru.microservices.web.constant.EmptyConstants;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.controller.BaseRestController;
import com.kuretru.microservices.web.entity.ApiResponse;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.web.libra.entity.query.LedgerCategoryQuery;
import com.kuretru.web.libra.entity.transfer.LedgerCategoryDTO;
import com.kuretru.web.libra.service.LedgerCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/ledgers/{ledgerId}/categories")
@RequireAuthorization
public class LedgerCategoryController extends BaseRestController<LedgerCategoryService, LedgerCategoryDTO, LedgerCategoryQuery> {

    @Autowired
    public LedgerCategoryController(LedgerCategoryService service) {
        super(service);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询记录")
    @Parameter(name = "id", description = "记录ID")
    @Override
    public ApiResponse<LedgerCategoryDTO> get(@PathVariable("id") UUID id) throws ServiceException {
        UUID userId = AccessTokenContext.getUserId();
        if (id == null || EmptyConstants.EMPTY_UUID.equals(id)) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定ID或ID错误");
        }
        LedgerCategoryDTO result = service.get(id,userId);
        if (null == result) {
            // 指定ID查询单个实体但实体不存在时，认为是用户方ID输入错误，因此抛异常
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "指定资源不存在");
        }
        return ApiResponse.success(result);
    }
}
