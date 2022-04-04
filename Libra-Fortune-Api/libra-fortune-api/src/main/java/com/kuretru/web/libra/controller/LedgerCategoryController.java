package com.kuretru.web.libra.controller;

import com.kuretru.microservices.authentication.annotaion.RequireAuthorization;
import com.kuretru.microservices.web.controller.BaseRestController;
import com.kuretru.web.libra.entity.query.LedgerCategoryQuery;
import com.kuretru.web.libra.entity.transfer.LedgerCategoryDTO;
import com.kuretru.web.libra.service.LedgerCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ledgers/{ledgerId}/categories")
@RequireAuthorization
public class LedgerCategoryController extends BaseRestController<LedgerCategoryService, LedgerCategoryDTO, LedgerCategoryQuery> {

    @Autowired
    public LedgerCategoryController(LedgerCategoryService service) {
        super(service);
    }

}
