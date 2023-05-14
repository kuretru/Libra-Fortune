package com.kuretru.web.libra.controller;

import com.kuretru.microservices.authentication.annotaion.RequireAuthorization;
import com.kuretru.microservices.web.controller.BaseRestController;
import com.kuretru.microservices.web.entity.ApiResponse;
import com.kuretru.microservices.web.entity.PaginationQuery;
import com.kuretru.microservices.web.entity.PaginationResponse;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.web.libra.entity.query.LedgerMemberQuery;
import com.kuretru.web.libra.entity.transfer.LedgerMemberDTO;
import com.kuretru.web.libra.entity.view.LedgerMemberVO;
import com.kuretru.web.libra.service.LedgerMemberService;
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
@RequestMapping("/ledgers/members")
@RequireAuthorization
public class LedgerMemberController
        extends BaseRestController<LedgerMemberService, LedgerMemberDTO, LedgerMemberQuery> {

    @Autowired
    public LedgerMemberController(LedgerMemberService service) {
        super(service);
    }

    @GetMapping
    @Override
    public ApiResponse<?> list(PaginationQuery paginationQuery, @Validated LedgerMemberQuery query) throws ServiceException {
        PaginationResponse<LedgerMemberVO> result = service.listVo(paginationQuery, query);
        if (result.getList() == null) {
            result.setList(new ArrayList<>());
        }
        if (result.getList().isEmpty()) {
            return ApiResponse.notFound(result);
        }
        return ApiResponse.success(result);
    }

}
