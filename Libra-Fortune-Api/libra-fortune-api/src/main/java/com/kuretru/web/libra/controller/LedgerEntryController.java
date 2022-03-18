package com.kuretru.web.libra.controller;

import com.kuretru.api.common.constant.EmptyConstants;
import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.controller.BaseCrudController;
import com.kuretru.api.common.controller.BaseRestController;
import com.kuretru.api.common.entity.ApiResponse;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.web.libra.entity.query.LedgerEntryQuery;
import com.kuretru.web.libra.entity.transfer.LedgerEntryDTO;
import com.kuretru.web.libra.service.LedgerEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
        if (!record.getLedgerId().toString().equals(ledgerId) || !record.getCategoryId().toString().equals(categoryId)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定记录");
        }
        return super.create(record);
    }


    @PutMapping("/{id}")
    @Operation(summary = "更新记录")
    @Parameter(name = "id", description = "记录ID")
    @Parameter(name = "record", description = "记录内容", required = true)
    @Override
    public ApiResponse<LedgerEntryDTO> update(@PathVariable("id") UUID id, @Validated @RequestBody LedgerEntryDTO record) throws ServiceException {
        if (id == null || EmptyConstants.EMPTY_UUID.equals(id)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定ID或ID错误");
        }
        if (record == null) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定记录");
        }
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