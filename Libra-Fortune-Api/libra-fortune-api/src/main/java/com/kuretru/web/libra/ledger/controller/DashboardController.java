package com.kuretru.web.libra.ledger.controller;

import com.kuretru.microservices.web.controller.BaseController;
import com.kuretru.microservices.web.entity.ApiResponse;
import com.kuretru.web.libra.ledger.entity.business.DashboardBO;
import com.kuretru.web.libra.ledger.entity.query.DashboardQuery;
import com.kuretru.web.libra.ledger.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ledgers/dashboards")
@Tag(name = "账本-面板")
public class DashboardController extends BaseController {

    private final DashboardService service;

    @Autowired
    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @PostMapping("/sum")
    @Operation(summary = "分组求和")
    public ApiResponse<List<DashboardBO>> sum(@RequestBody @Validated DashboardQuery query) {
        return ApiResponse.success(service.sum(query));
    }

}
