package com.kuretru.web.libra.controller;

import com.kuretru.api.common.controller.BaseRestController;
import com.kuretru.web.libra.entity.query.LedgerTagQuery;
import com.kuretru.web.libra.entity.transfer.LedgerTagDTO;
import com.kuretru.web.libra.service.LedgerTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ledger/tags")
public class LedgerTagController extends BaseRestController<LedgerTagService, LedgerTagDTO, LedgerTagQuery> {

    @Autowired
    public LedgerTagController(LedgerTagService service) {
        super(service);
    }


}
