package com.kuretru.web.libra.metadata.controller;

import com.kuretru.microservices.common.entity.enums.EnumDTO;
import com.kuretru.microservices.web.entity.ApiResponse;
import com.kuretru.microservices.web.v2.controller.BaseRestController;
import com.kuretru.microservices.web.v2.entity.query.EmptyQuery;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataCurrencyDTO;
import com.kuretru.web.libra.metadata.service.MetadataCurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/metadata/currencies")
@Tag(name = "元数据-货币单位")
public class MetadataCurrencyController
        extends BaseRestController<MetadataCurrencyService, MetadataCurrencyDTO, EmptyQuery> {

    @Autowired
    public MetadataCurrencyController(MetadataCurrencyService service) {
        super(service);
    }

    @GetMapping("/enums")
    @Operation(summary = "枚举值")
    public ApiResponse<List<EnumDTO>> enums() {
        return ApiResponse.success(service.enums());
    }

}
