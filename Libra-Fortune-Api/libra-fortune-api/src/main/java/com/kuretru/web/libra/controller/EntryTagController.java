package com.kuretru.web.libra.controller;

import com.kuretru.api.common.constant.EmptyConstants;
import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.controller.BaseCrudController;
import com.kuretru.api.common.entity.ApiResponse;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.web.libra.entity.query.EntryTagQuery;
import com.kuretru.web.libra.entity.transfer.EntryTagDTO;
import com.kuretru.web.libra.service.EntryTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/entries/{entryId}/tags/{tagId}")
public class EntryTagController extends BaseCrudController<EntryTagService, EntryTagDTO, EntryTagQuery> {

    @Autowired
    public EntryTagController(EntryTagService service) {
        super(service);
    }

    @PostMapping
    public ApiResponse<EntryTagDTO> create(@PathVariable("entryId") String entryId, @PathVariable("tagId") String tagId, @RequestBody EntryTagDTO record) throws ServiceException {
        if (record == null || !record.getEntryId().toString().equals(entryId) || !record.getTagId().toString().equals(tagId)) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定记录");
        }
        return super.create(record);
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