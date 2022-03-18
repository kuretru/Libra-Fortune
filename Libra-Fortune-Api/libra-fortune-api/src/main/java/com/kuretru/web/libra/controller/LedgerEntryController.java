package com.kuretru.web.libra.controller;

import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.controller.BaseCrudController;
import com.kuretru.api.common.controller.BaseRestController;
import com.kuretru.api.common.entity.ApiResponse;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.web.libra.entity.query.LedgerEntryQuery;
import com.kuretru.web.libra.entity.transfer.LedgerEntryDTO;
import com.kuretru.web.libra.service.LedgerEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/ledgers/{ledgerId}/categories/{categoryId}/common/entries")
public class LedgerEntryController extends BaseCrudController<LedgerEntryService, LedgerEntryDTO, LedgerEntryQuery> {

    @Autowired
    public LedgerEntryController(LedgerEntryService service) {
        super(service);
    }

    @PostMapping
    public ApiResponse<LedgerEntryDTO> create(@PathVariable("ledgerId") String ledgerId, @PathVariable("categoryId") String categoryId, @RequestBody LedgerEntryDTO record) throws ServiceException {
        if (!record.getLedgerId().toString().equals(ledgerId) || record.getCategoryId().toString().equals(categoryId)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定记录");
        }
        return super.create(record);
    }


}