package com.kuretru.web.libra.account.controller;

import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.entity.ApiResponse;
import com.kuretru.microservices.web.entity.PaginationQuery;
import com.kuretru.microservices.web.entity.PaginationResponse;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.v2.controller.BaseCrudController;
import com.kuretru.web.libra.account.entity.query.AccountBalanceQuery;
import com.kuretru.web.libra.account.entity.transfer.AccountBalanceDTO;
import com.kuretru.web.libra.account.service.AccountBalanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts/{accountId}/balances")
@Tag(name = "账户-余额快照")
public class AccountBalanceController
        extends BaseCrudController<AccountBalanceService, AccountBalanceDTO, AccountBalanceQuery> {

    @Autowired
    public AccountBalanceController(AccountBalanceService service) {
        super(service);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询记录")
    public ApiResponse<AccountBalanceDTO> get(@Parameter(description = "记录ID") @PathVariable Long id)
            throws ServiceException {
        if (id == null || id == 0) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定ID或ID错误");
        }
        return super.get(id);
    }

    @GetMapping
    @Operation(summary = "根据分页参数和查询条件查询记录列表")
    public ApiResponse<PaginationResponse<AccountBalanceDTO>> list(
            @Parameter(description = "账户ID") @PathVariable Long accountId,
            @ParameterObject @Validated PaginationQuery paginationQuery,
            @ParameterObject @Validated AccountBalanceQuery query) throws ServiceException {
        query.setAccountId(accountId);
        return super.listByPage(paginationQuery, query);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "创建新记录")
    public ApiResponse<AccountBalanceDTO> create(
            @Parameter(description = "账户ID") @PathVariable Long accountId,
            @Parameter(description = "记录内容", required = true) @Validated @RequestBody AccountBalanceDTO record)
            throws ServiceException {
        if (accountId == null || accountId == 0) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定账户ID或账户ID错误");
        } else if (record == null) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定记录");
        }
        record.setAccountId(accountId);
        return super.create(record);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新记录")
    public ApiResponse<AccountBalanceDTO> update(
            @Parameter(description = "账户ID") @PathVariable Long accountId,
            @Parameter(description = "记录ID") @PathVariable Long id,
            @Parameter(description = "记录内容", required = true) @Validated @RequestBody AccountBalanceDTO record)
            throws ServiceException {
        if (accountId == null || accountId == 0) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定账户ID或账户ID错误");
        } else if (id == null || id == 0) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定ID或ID错误");
        } else if (record == null) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定记录");
        }
        record.setId(id);
        record.setAccountId(accountId);
        return ApiResponse.updated(service.update(record));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "根据ID删除记录")
    public ApiResponse<String> remove(@Parameter(description = "记录ID") @PathVariable Long id)
            throws ServiceException {
        if (id == null || id == 0) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定ID或ID错误");
        }
        return super.remove(id);
    }

}
