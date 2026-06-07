package com.kuretru.web.libra.tools.controller;

import com.kuretru.microservices.web.controller.BaseController;
import com.kuretru.microservices.web.entity.ApiResponse;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.web.libra.tools.entity.transfer.CalculatorDTO;
import com.kuretru.web.libra.tools.entity.transfer.CalculatorResult;
import com.kuretru.web.libra.tools.service.CalculatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tools/calculator")
@Tag(name = "工具-计算器")
public class CalculatorController extends BaseController {

    private final CalculatorService service;

    @Autowired
    public CalculatorController(CalculatorService service) {
        this.service = service;
    }

    @PostMapping("/add")
    @Operation(summary = "加法")
    public ApiResponse<CalculatorResult> add(
            @Parameter(description = "计算参数", required = true) @Validated @RequestBody CalculatorDTO record) {
        return ApiResponse.success(service.add(record));
    }

    @PostMapping("/subtract")
    @Operation(summary = "减法")
    public ApiResponse<CalculatorResult> subtract(
            @Parameter(description = "计算参数", required = true) @Validated @RequestBody CalculatorDTO record) {
        return ApiResponse.success(service.subtract(record));
    }

    @PostMapping("/multiply")
    @Operation(summary = "乘法")
    public ApiResponse<CalculatorResult> multiply(
            @Parameter(description = "计算参数", required = true) @Validated @RequestBody CalculatorDTO record) {
        return ApiResponse.success(service.multiply(record));
    }

    @PostMapping("/divide")
    @Operation(summary = "除法")
    public ApiResponse<CalculatorResult> divide(
            @Parameter(description = "计算参数", required = true) @Validated @RequestBody CalculatorDTO record)
            throws ServiceException {
        return ApiResponse.success(service.divide(record));
    }

}
