package com.kuretru.web.libra.metadata.controller;

import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.entity.ApiResponse;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.controller.BaseSequencedRestController;
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

import java.util.List;

@RestController
@RequestMapping("/api/metadata/tag_sets")
@Tag(name = "元数据-账本标签组")
public class MetadataTagSetController extends BaseSequencedRestController<MetadataTagSetService, MetadataTagSetDTO, MetadataTagSetQuery> {

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

    @PutMapping("/{setId}/items/reorder")
    @Operation(summary = "重新排序标签组下的标签")
    @Parameter(name = "idList", description = "新顺序的记录ID列表")
    public ApiResponse<String> reorderItems(@Parameter(description = "标签组ID") @PathVariable Long setId, @RequestBody List<Long> idList) throws ServiceException {
        if (idList == null || idList.isEmpty()) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定ID列表");
        }

        var itemIdList = itemService.listByParentId(setId).stream()
                .map(MetadataTagSetItemDTO::getId)
                .toList();
        if (!itemIdList.containsAll(idList)) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "标签ID不属于指定标签组");
        }

        itemService.reorder(idList);
        return ApiResponse.success("重新排序成功");
    }

}
