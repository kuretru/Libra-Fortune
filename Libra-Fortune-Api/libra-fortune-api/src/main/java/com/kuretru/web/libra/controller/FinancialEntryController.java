package com.kuretru.web.libra.controller;

import com.kuretru.api.common.controller.BaseRestController;
import com.kuretru.web.libra.entity.query.FinancialEntryQuery;
import com.kuretru.web.libra.entity.transfer.FinancialEntryDTO;
import com.kuretru.web.libra.service.FinancialEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ledgers/{ledgerId}/financial/entries")
public class FinancialEntryController extends BaseRestController<FinancialEntryService, FinancialEntryDTO, FinancialEntryQuery> {

    @Autowired
    public FinancialEntryController(FinancialEntryService service) {
        super(service);
    }

}