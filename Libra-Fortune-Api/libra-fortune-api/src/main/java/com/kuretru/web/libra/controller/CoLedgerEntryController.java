package com.kuretru.web.libra.controller;

import com.kuretru.api.common.controller.BaseRestController;
import com.kuretru.web.libra.entity.query.CoLedgerUserQuery;
import com.kuretru.web.libra.entity.transfer.CoLedgerUserDTO;
import com.kuretru.web.libra.service.CoLedgerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/co/ledgers-users")
public class CoLedgerEntryController extends BaseRestController<CoLedgerUserService, CoLedgerUserDTO, CoLedgerUserQuery> {

    @Autowired
    public CoLedgerEntryController(CoLedgerUserService service) {
        super(service);
    }

}