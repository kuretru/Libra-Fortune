package com.kuretru.web.libra.dashboard.controller;

import com.kuretru.microservices.web.controller.BaseController;
import com.kuretru.microservices.web.entity.ApiResponse;
import com.kuretru.web.libra.dashboard.entity.business.DashboardAccountBalanceBO;
import com.kuretru.web.libra.dashboard.entity.business.DashboardLedgerBO;
import com.kuretru.web.libra.dashboard.entity.query.DashboardQuery;
import com.kuretru.web.libra.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboards")
@Tag(name = "面板")
public class DashboardController extends BaseController {

    private final DashboardService service;

    @Autowired
    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @PostMapping("/ledgers")
    @Operation(summary = "账本条目看板")
    public ApiResponse<List<DashboardLedgerBO>> ledger(@RequestBody @Validated DashboardQuery query) {
        return ApiResponse.success(service.ledger(query));
    }

    @GetMapping("/account-balances/latest")
    @Operation(summary = "最新账户余额快照")
    public ApiResponse<DashboardAccountBalanceBO> latestAccountBalances() {
        return ApiResponse.success(service.latestAccountBalances());
    }

}
