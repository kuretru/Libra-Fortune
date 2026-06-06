package com.kuretru.web.libra.ledger.controller;

import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.entity.ApiResponse;
import com.kuretru.microservices.web.entity.PaginationQuery;
import com.kuretru.microservices.web.entity.PaginationResponse;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.v2.controller.BaseRestController;
import com.kuretru.web.libra.ledger.entity.query.LedgerEntryQuery;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerEntryDTO;
import com.kuretru.web.libra.ledger.service.LedgerEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@RestController("ledgerV2EntryController")
@RequestMapping("/api/ledgers/{ledgerId}/entries")
@Tag(name = "账本-条目")
public class LedgerEntryController extends BaseRestController<LedgerEntryService, LedgerEntryDTO, LedgerEntryQuery> {

    @Autowired
    public LedgerEntryController(LedgerEntryService service) {
        super(service);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询记录")
    @Override
    public ApiResponse<LedgerEntryDTO> get(@Parameter(description = "记录ID") @PathVariable Long id) throws ServiceException {
        var response = super.get(id);
        verifyLedgerId(getLedgerId(), response.getData());
        return response;
    }

    @GetMapping
    @Operation(summary = "根据分页参数和查询条件查询记录列表")
    @Override
    public ApiResponse<PaginationResponse<LedgerEntryDTO>> list(@ParameterObject @Validated PaginationQuery paginationQuery,
                                                                @ParameterObject @Validated LedgerEntryQuery query) throws ServiceException {
        query.setLedgerId(getLedgerId());
        return super.list(paginationQuery, query);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "创建新记录")
    @Override
    public ApiResponse<LedgerEntryDTO> create(@Parameter(description = "记录内容", required = true) @Validated @RequestBody LedgerEntryDTO record) throws ServiceException {
        if (record == null) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定记录");
        }
        record.setLedgerId(getLedgerId());
        return super.create(record);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新记录")
    @Override
    public ApiResponse<LedgerEntryDTO> update(@Parameter(description = "记录ID") @PathVariable Long id,
                                              @Parameter(description = "记录内容", required = true) @Validated @RequestBody LedgerEntryDTO record) throws ServiceException {
        if (record == null) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "未指定记录");
        }
        record.setLedgerId(getLedgerId());
        return super.update(id, record);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "根据ID删除记录")
    @Override
    public ApiResponse<String> remove(@Parameter(description = "记录ID") @PathVariable Long id) throws ServiceException {
        var record = service.get(id);
        verifyLedgerId(getLedgerId(), record);
        return super.remove(id);
    }

    @SuppressWarnings("unchecked")
    private Long getLedgerId() {
        var requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        Map<String, String> variables = (Map<String, String>) requestAttributes.getRequest()
                .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        return Long.valueOf(variables.get("ledgerId"));
    }

    private void verifyLedgerId(Long ledgerId, LedgerEntryDTO record) {
        if (record == null) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "指定资源不存在");
        }
        if (!ledgerId.equals(record.getLedgerId())) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "指定条目不属于该账本");
        }
    }

}
