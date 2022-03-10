package com.kuretru.web.libra.controller;

import com.kuretru.api.common.controller.BaseRestController;
import com.kuretru.web.libra.entity.query.LedgerQuery;
import com.kuretru.web.libra.entity.query.SystemUserQuery;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.entity.transfer.SystemUserDTO;
import com.kuretru.web.libra.service.LedgerService;
import com.kuretru.web.libra.service.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ledger")
public class LedgerController extends BaseRestController<LedgerService, LedgerDTO, LedgerQuery> {

    @Autowired
    public LedgerController(LedgerService service) {
        super(service);
    }
}