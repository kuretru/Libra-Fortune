package com.kuretru.web.libra.controller;

import com.kuretru.api.common.constant.EmptyConstants;
import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.controller.BaseCrudController;
import com.kuretru.api.common.controller.BaseRestController;
import com.kuretru.api.common.entity.ApiResponse;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.web.libra.entity.query.CoLedgerUserQuery;
import com.kuretru.web.libra.entity.transfer.CoLedgerUserDTO;
import com.kuretru.web.libra.service.CoLedgerUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/co-ledgers/{ledgerId}/users")
public class CoLedgerUserController extends BaseCrudController<CoLedgerUserService, CoLedgerUserDTO, CoLedgerUserQuery> {

    @Autowired
    public CoLedgerUserController(CoLedgerUserService service) {
        super(service);
    }

    @PostMapping
    public ApiResponse<CoLedgerUserDTO> create(@PathVariable("ledgerId") String ledgerId, CoLedgerUserDTO record) throws ServiceException {
        if (!record.getLedgerId().toString().equals(ledgerId)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定记录");
        }
        return super.create(record);
    }

    @PutMapping("/{id}")
    public ApiResponse<CoLedgerUserDTO> update(@PathVariable("ledgerId") String ledgerId, UUID id, CoLedgerUserDTO record) throws ServiceException {
        if (!record.getLedgerId().toString().equals(ledgerId)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定记录");
        }
        return super.update(id, record);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "根据ID删除记录")
    @Parameter(name = "id", description = "记录ID")
    public ApiResponse<String> remove(@PathVariable("id") UUID id) throws ServiceException {
        if (id == null || EmptyConstants.EMPTY_UUID.equals(id)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定ID或ID错误");
        }
        return super.remove(id);
    }
}