package com.kuretru.web.libra.controller;

import com.kuretru.api.common.constant.EmptyConstants;
import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.controller.BaseCrudController;
import com.kuretru.api.common.controller.BaseRestController;
import com.kuretru.api.common.entity.ApiResponse;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.web.libra.entity.query.CoLedgerEntryQuery;
import com.kuretru.web.libra.entity.transfer.CoLedgerEntryDTO;
import com.kuretru.web.libra.service.CoLedgerEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
    public ApiResponse<CoLedgerEntryDTO> create(@PathVariable("ledgerId") UUID ledgerId, @PathVariable("entryId") UUID entryId, @RequestBody CoLedgerEntryDTO record) throws ServiceException {
//        !record.getUserId().equals(userId);
        if (record == null) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定记录");
        }
        if (!record.getEntryId().toString().equals(entryId.toString())) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定记录");
        }
        CoLedgerEntryDTO result = service.save(ledgerId.toString(), record);
        return ApiResponse.created(result);
    }

    @PutMapping("/{id}")
    public ApiResponse<CoLedgerEntryDTO> update(@PathVariable("id") UUID id, @Validated @RequestBody CoLedgerEntryDTO record) throws ServiceException {
        return super.update(id, record);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "根据ID删除记录")
    @Parameter(name = "id", description = "记录ID")
    @Override
    public ApiResponse<String> remove(@PathVariable("id") UUID id) throws ServiceException {
        if (id == null || EmptyConstants.EMPTY_UUID.equals(id)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定ID或ID错误");
        }
        return super.remove(id);
    }
}