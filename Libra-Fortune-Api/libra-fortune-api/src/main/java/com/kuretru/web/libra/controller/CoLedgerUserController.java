package com.kuretru.web.libra.controller;

import com.kuretru.microservices.authentication.annotaion.RequireAuthorization;
import com.kuretru.microservices.web.controller.BaseRestController;
import com.kuretru.web.libra.entity.query.CoLedgerUserQuery;
import com.kuretru.web.libra.entity.transfer.CoLedgerUserDTO;
import com.kuretru.web.libra.service.CoLedgerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ledgers/{ledgerId}/users")
@RequireAuthorization
public class CoLedgerUserController extends BaseRestController<CoLedgerUserService, CoLedgerUserDTO, CoLedgerUserQuery> {

    @Autowired
    public CoLedgerUserController(CoLedgerUserService service) {
        super(service);
    }

}
