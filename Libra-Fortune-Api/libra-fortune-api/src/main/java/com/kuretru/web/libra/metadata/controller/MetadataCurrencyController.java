package com.kuretru.web.libra.metadata.controller;

import com.kuretru.microservices.common.entity.enums.EnumDTO;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.entity.ApiResponse;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.v2.controller.BaseRestController;
import com.kuretru.microservices.web.v2.controller.BaseSequencedRestController;
import com.kuretru.microservices.web.v2.entity.query.EmptyQuery;
import com.kuretru.web.libra.metadata.entity.transfer.MetadataCurrencyDTO;
import com.kuretru.web.libra.metadata.service.MetadataCurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metadata/currencies")
@Tag(name = "元数据-货币单位")
public class MetadataCurrencyController
        extends BaseSequencedRestController<MetadataCurrencyService, MetadataCurrencyDTO, EmptyQuery> {

    @Autowired
    public MetadataCurrencyController(MetadataCurrencyService service) {
        super(service);
    }

    @GetMapping("/enums")
    @Operation(summary = "枚举值")
    public ApiResponse<List<EnumDTO<String>>> enums() {
        return ApiResponse.success(service.enums());
    }

    @PutMapping("/reorder")
    @Operation(summary = "重新排序记录")
    @Parameter(name = "idList", description = "新顺序的记录ID列表")
    public ApiResponse<String> reorder(@RequestBody List<Long> idList) throws ServiceException {
        if (idList == null || idList.isEmpty()) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定ID列表");
        }
        service.reorder(idList);
        return ApiResponse.success("重新排序成功");
    }

}
