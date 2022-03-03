package com.kuretru.web.libra.controller;

import com.kuretru.api.common.controller.BaseRestController;
import com.kuretru.web.libra.entity.query.SysUserQuery;
import com.kuretru.web.libra.entity.transfer.SysUserDTO;
import com.kuretru.web.libra.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class SysUserController extends BaseRestController<SysUserService, SysUserDTO, SysUserQuery> {

    @Autowired
    public SysUserController(SysUserService service) {
        super(service);
    }
}
