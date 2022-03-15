package com.kuretru.web.libra.controller;

import com.kuretru.api.common.controller.BaseRestController;
import com.kuretru.api.common.entity.ApiResponse;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.web.libra.entity.query.CoLedgerUserQuery;
import com.kuretru.web.libra.entity.transfer.CoLedgerUserDTO;
import com.kuretru.web.libra.service.CoLedgerUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/co/ledgers-users")
public class CoLedgerUserController extends BaseRestController<CoLedgerUserService, CoLedgerUserDTO, CoLedgerUserQuery> {

    @Autowired
    public CoLedgerUserController(CoLedgerUserService service) {
        super(service);
    }

}