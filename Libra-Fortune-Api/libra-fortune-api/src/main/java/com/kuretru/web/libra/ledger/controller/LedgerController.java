package com.kuretru.web.libra.ledger.controller;

import com.kuretru.microservices.web.entity.ApiResponse;
import com.kuretru.microservices.web.v2.controller.BaseRestController;
import com.kuretru.web.libra.ledger.entity.query.LedgerQuery;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerEnumDTO;
import com.kuretru.web.libra.ledger.service.LedgerEnumService;
import com.kuretru.web.libra.ledger.service.LedgerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ledgers")
@Tag(name = "账本")
public class LedgerController extends BaseRestController<LedgerService, LedgerDTO, LedgerQuery> {

    private final LedgerEnumService enumService;

    @Autowired
    public LedgerController(LedgerService service, LedgerEnumService enumService) {
        super(service);
        this.enumService = enumService;
    }

    @GetMapping("/enums")
    @Operation(summary = "账本领域枚举")
    public ApiResponse<LedgerEnumDTO> enums() {
        return ApiResponse.success(enumService.enums());
    }

}
