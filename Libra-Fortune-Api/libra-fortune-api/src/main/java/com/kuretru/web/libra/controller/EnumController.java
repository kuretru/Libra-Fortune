package com.kuretru.web.libra.controller;

import com.kuretru.microservices.common.utils.EnumUtils;
import com.kuretru.microservices.web.controller.BaseController;
import com.kuretru.microservices.web.entity.ApiResponse;
import com.kuretru.web.libra.entity.enums.LedgerTypeEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@RestController
public class EnumController extends BaseController {

    @GetMapping("/ledgers/types")
    public ApiResponse<?> getLedgerType() {
        return ApiResponse.success(EnumUtils.buildDTO(LedgerTypeEnum.values()));
    }

}
