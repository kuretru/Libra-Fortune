package com.kuretru.web.libra.controller;

import com.kuretru.api.common.constant.EmptyConstants;
import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.controller.BaseRestController;
import com.kuretru.api.common.entity.ApiResponse;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.web.libra.entity.query.LedgerCategoryQuery;
import com.kuretru.web.libra.entity.query.SysUserQuery;
import com.kuretru.web.libra.entity.transfer.LedgerCategoryDTO;
import com.kuretru.web.libra.entity.transfer.SysUserDTO;
import com.kuretru.web.libra.service.LedgerCategoryService;
import com.kuretru.web.libra.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class SysUserController extends BaseRestController<SysUserService, SysUserDTO, SysUserQuery> {

    @Autowired
    public SysUserController(SysUserService service) {
        super(service);
    }
}
