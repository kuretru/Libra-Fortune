package com.kuretru.web.libra.controller;

import com.kuretru.microservices.authentication.annotaion.RequireAuthorization;
import com.kuretru.microservices.web.controller.BaseRestController;
import com.kuretru.web.libra.entity.query.LedgerEntryCategoryQuery;
import com.kuretru.web.libra.entity.transfer.LedgerEntryCategoryDTO;
import com.kuretru.web.libra.service.LedgerEntryCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@RestController
@RequestMapping("/ledgers/{ledger_id}/categories")
@RequireAuthorization
public class LedgerEntryCategoryController extends BaseRestController<LedgerEntryCategoryService, LedgerEntryCategoryDTO, LedgerEntryCategoryQuery> {

    @Autowired
    public LedgerEntryCategoryController(LedgerEntryCategoryService service) {
        super(service);
    }

}
