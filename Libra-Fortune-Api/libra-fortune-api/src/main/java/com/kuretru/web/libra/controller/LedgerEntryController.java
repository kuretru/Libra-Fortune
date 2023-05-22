package com.kuretru.web.libra.controller;

import com.kuretru.microservices.authentication.annotaion.RequireAuthorization;
import com.kuretru.microservices.web.controller.BaseRestController;
import com.kuretru.microservices.web.entity.ApiResponse;
import com.kuretru.microservices.web.entity.PaginationQuery;
import com.kuretru.microservices.web.entity.PaginationResponse;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.web.libra.entity.query.LedgerEntryQuery;
import com.kuretru.web.libra.entity.transfer.LedgerEntryDTO;
import com.kuretru.web.libra.entity.view.LedgerEntryVO;
import com.kuretru.web.libra.service.LedgerEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@RestController
@RequestMapping("/ledgers/{ledgerId}/entries")
@RequireAuthorization
public class LedgerEntryController
        extends BaseRestController<LedgerEntryService, LedgerEntryDTO, LedgerEntryQuery> {

    @Autowired
    public LedgerEntryController(LedgerEntryService service) {
        super(service);
    }

    @GetMapping
    @Override
    public ApiResponse<?> list(PaginationQuery paginationQuery, @Validated LedgerEntryQuery query) throws ServiceException {
        PaginationResponse<LedgerEntryVO> result = service.listVo(paginationQuery, query);
        if (result.getList() == null) {
            result.setList(new ArrayList<>());
        }
        if (PaginationQuery.isNull(paginationQuery)) {
            return ApiResponse.success(result.getList());
        }
        return ApiResponse.success(result);
    }

}
