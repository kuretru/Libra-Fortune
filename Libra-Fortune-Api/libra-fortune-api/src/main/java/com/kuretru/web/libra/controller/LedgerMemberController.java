package com.kuretru.web.libra.controller;

import com.kuretru.microservices.authentication.annotaion.RequireAuthorization;
import com.kuretru.microservices.web.controller.BaseRestController;
import com.kuretru.web.libra.entity.query.LedgerMemberQuery;
import com.kuretru.web.libra.entity.transfer.LedgerMemberDTO;
import com.kuretru.web.libra.service.LedgerMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@RestController
@RequestMapping("/ledgers/{ledger_id}/members")
@RequireAuthorization
public class LedgerMemberController extends BaseRestController<LedgerMemberService, LedgerMemberDTO, LedgerMemberQuery> {

    @Autowired
    public LedgerMemberController(LedgerMemberService service) {
        super(service);
    }

}
