package com.kuretru.web.libra.metadata.controller;

import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.entity.ApiResponse;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.v2.controller.BaseRestController;
import com.kuretru.web.libra.metadata.entity.query.MetadataTagSetQuery;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataTagSetDTO;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataTagSetItemDTO;
import com.kuretru.web.libra.metadata.service.MetadataTagSetItemService;
import com.kuretru.web.libra.metadata.service.MetadataTagSetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/metadata/tag_sets")
@Tag(name = "元数据-账本标签组")
public class MetadataTagSetController extends BaseRestController<MetadataTagSetService, MetadataTagSetDTO, MetadataTagSetQuery> {

    private final MetadataTagSetItemService itemService;

    @Autowired
    public MetadataTagSetController(MetadataTagSetService service, MetadataTagSetItemService itemService) {
        super(service);
        this.itemService = itemService;
    }

    @PostMapping("/{setId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "创建新记录")
    public ApiResponse<MetadataTagSetItemDTO> create(@Parameter(description = "标签组ID") @PathVariable Long setId, @Parameter(description = "记录内容", required = true) @Validated @RequestBody MetadataTagSetItemDTO record) throws ServiceException {
        if (record == null) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定记录");
        }
        record.setSetId(setId);
        var result = itemService.save(record);
        return ApiResponse.created(result);
    }

    @PutMapping("/{setId}/items/{id}")
    @Operation(summary = "更新记录")
    public ApiResponse<MetadataTagSetItemDTO> update(@Parameter(description = "标签组ID") @PathVariable Long setId, @Parameter(description = "记录ID") @PathVariable Long id, @Parameter(description = "记录内容", required = true) @Validated @RequestBody MetadataTagSetItemDTO record) throws ServiceException {
        if (id == null || id == 0) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定ID或ID错误");
        } else if (record == null) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定记录");
        } else if (!id.equals(record.getId())) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "指定ID与记录ID不符");
        }
        record.setId(id);
        var result = itemService.update(record);
        return ApiResponse.updated(result);
    }

    @DeleteMapping("/{setId}/items/{id}")
    @Operation(summary = "根据ID删除记录")
    public ApiResponse<String> remove(@Parameter(description = "记录ID") @PathVariable Long id) throws ServiceException {
        if (id == null || id == 0) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定ID或ID错误");
        }
        itemService.remove(id);
        return ApiResponse.removed("资源已删除");
    }

}
