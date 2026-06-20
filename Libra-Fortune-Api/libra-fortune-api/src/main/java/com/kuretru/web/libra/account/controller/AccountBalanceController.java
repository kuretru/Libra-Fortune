package com.kuretru.web.libra.account.controller;

import com.kuretru.microservices.web.controller.BaseController;
import com.kuretru.microservices.web.entity.ApiResponse;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.web.libra.account.entity.business.AccountBalanceResultBO;
import com.kuretru.web.libra.account.entity.query.AccountBalanceQuery;
import com.kuretru.web.libra.account.service.AccountBalanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "账户-余额快照")
public class AccountBalanceController extends BaseController {

    private final AccountBalanceService service;

    @Autowired
    public AccountBalanceController(AccountBalanceService service) {
        this.service = service;
    }

    @GetMapping("/balances")
    @Operation(summary = "查询账户余额记录")
    public ApiResponse<AccountBalanceResultBO> list(@ParameterObject @Validated AccountBalanceQuery query)
            throws ServiceException {
        return ApiResponse.success(service.list(query));
    }

}
