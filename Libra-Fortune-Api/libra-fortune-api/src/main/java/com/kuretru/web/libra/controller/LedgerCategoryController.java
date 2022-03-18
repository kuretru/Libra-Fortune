package com.kuretru.web.libra.controller;

import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.controller.BaseCrudController;
import com.kuretru.api.common.controller.BaseRestController;
import com.kuretru.api.common.entity.ApiResponse;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.web.libra.entity.query.LedgerCategoryQuery;
import com.kuretru.web.libra.entity.transfer.LedgerCategoryDTO;
import com.kuretru.web.libra.service.LedgerCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/ledgers/{ledgerId}/categories")
public class LedgerCategoryController extends BaseCrudController<LedgerCategoryService, LedgerCategoryDTO, LedgerCategoryQuery> {

    @Autowired
    public LedgerCategoryController(LedgerCategoryService service) {
        super(service);
    }

    @PostMapping
    public ApiResponse<LedgerCategoryDTO> create(@PathVariable("ledgerId") String ledgerId, @RequestBody LedgerCategoryDTO record) throws ServiceException {
        if (!record.getLedgerId().toString().equals(ledgerId)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定记录");
        }
        return super.create(record);
    }

}