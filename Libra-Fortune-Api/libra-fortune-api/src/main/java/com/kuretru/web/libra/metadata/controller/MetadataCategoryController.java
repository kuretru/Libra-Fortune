package com.kuretru.web.libra.metadata.controller;

import com.kuretru.microservices.common.entity.enums.EnumDTO;
import com.kuretru.microservices.web.entity.ApiResponse;
import com.kuretru.microservices.web.v2.controller.BaseRestController;
import com.kuretru.microservices.web.v2.entity.query.EmptyQuery;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataCategoryDTO;
import com.kuretru.web.libra.metadata.service.MetadataCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/metadata/categories")
@Tag(name = "元数据-账本分类")
public class MetadataCategoryController
        extends BaseRestController<MetadataCategoryService, MetadataCategoryDTO, EmptyQuery> {

    @Autowired
    public MetadataCategoryController(MetadataCategoryService service) {
        super(service);
    }

    @GetMapping("/enums")
    @Operation(summary = "枚举值")
    public ApiResponse<List<EnumDTO>> enums() {
        return ApiResponse.success(service.enums());
    }

}
