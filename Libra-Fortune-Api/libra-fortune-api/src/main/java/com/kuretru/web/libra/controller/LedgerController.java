package com.kuretru.web.libra.controller;

import com.kuretru.microservices.authentication.annotaion.RequireAuthorization;
import com.kuretru.microservices.web.controller.BaseRestController;
import com.kuretru.microservices.web.entity.ApiResponse;
import com.kuretru.microservices.web.entity.PaginationQuery;
import com.kuretru.microservices.web.entity.PaginationResponse;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.web.libra.entity.query.LedgerQuery;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.entity.view.LedgerVO;
import com.kuretru.web.libra.service.LedgerService;
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
@RequestMapping("/ledgers")
@RequireAuthorization
public class LedgerController
        extends BaseRestController<LedgerService, LedgerDTO, LedgerQuery> {

    @Autowired
    public LedgerController(LedgerService service) {
        super(service);
    }

    @GetMapping
    @Override
    public ApiResponse<?> list(PaginationQuery paginationQuery, @Validated LedgerQuery query) throws ServiceException {
        PaginationResponse<LedgerVO> result = service.listVo(paginationQuery, query);
        if (result.getList() == null) {
            result.setList(new ArrayList<>());
        }
        if (result.getList().isEmpty()) {
            return ApiResponse.notFound(result);
        }
        return ApiResponse.success(result);
    }

}
