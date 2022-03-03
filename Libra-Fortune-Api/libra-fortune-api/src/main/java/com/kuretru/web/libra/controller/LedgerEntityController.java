package com.kuretru.web.libra.controller;

import com.kuretru.api.common.controller.BaseRestController;
import com.kuretru.web.libra.entity.query.LedgerEntityQuery;
import com.kuretru.web.libra.entity.transfer.LedgerEntityDTO;
import com.kuretru.web.libra.service.LedgerEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ledger/entities")
public class LedgerEntityController extends BaseRestController<LedgerEntityService, LedgerEntityDTO, LedgerEntityQuery> {

    @Autowired
    public LedgerEntityController(LedgerEntityService service) {
        super(service);
    }


}
