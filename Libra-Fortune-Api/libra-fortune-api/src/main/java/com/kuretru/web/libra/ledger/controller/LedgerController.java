package com.kuretru.web.libra.ledger.controller;

import com.kuretru.microservices.web.v2.controller.BaseRestController;
import com.kuretru.web.libra.ledger.entity.query.LedgerQuery;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.ledger.service.LedgerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("ledgerV2Controller")
@RequestMapping("/api/ledgers")
@Tag(name = "账本")
public class LedgerController extends BaseRestController<LedgerService, LedgerDTO, LedgerQuery> {

    @Autowired
    public LedgerController(LedgerService service) {
        super(service);
    }

}
