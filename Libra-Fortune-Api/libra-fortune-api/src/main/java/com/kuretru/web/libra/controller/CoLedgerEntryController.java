package com.kuretru.web.libra.controller;

import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.controller.BaseCrudController;
import com.kuretru.api.common.controller.BaseRestController;
import com.kuretru.api.common.entity.ApiResponse;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.web.libra.entity.query.CoLedgerEntryQuery;
import com.kuretru.web.libra.entity.transfer.CoLedgerEntryDTO;
import com.kuretru.web.libra.service.CoLedgerEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/co-ledgers/{ledgerId}/entries/{entryId}")
public class CoLedgerEntryController extends BaseCrudController<CoLedgerEntryService, CoLedgerEntryDTO, CoLedgerEntryQuery> {

    @Autowired
    public CoLedgerEntryController(CoLedgerEntryService service) {
        super(service);
    }

    @PostMapping
    public ApiResponse<CoLedgerEntryDTO> create(@PathVariable("ledgerId") String ledgerId, @PathVariable("entryId") String entryId, CoLedgerEntryDTO record) throws ServiceException {
//        !record.getUserId().equals(userId);
        if (record == null) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定记录");
        }
        if (!record.getEntryId().toString().equals(entryId)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定记录");
        }
        CoLedgerEntryDTO result = service.save(ledgerId, record);
        return ApiResponse.created(result);
    }

    @PutMapping("/{id}")
    public ApiResponse<CoLedgerEntryDTO> update(UUID id, CoLedgerEntryDTO record) throws ServiceException {
        return super.update(id, record);
    }

}