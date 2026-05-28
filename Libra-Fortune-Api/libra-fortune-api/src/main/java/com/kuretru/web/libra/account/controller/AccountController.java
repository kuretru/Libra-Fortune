package com.kuretru.web.libra.account.controller;

import com.kuretru.microservices.web.v2.controller.BaseRestController;
import com.kuretru.web.libra.account.entity.query.AccountQuery;
import com.kuretru.web.libra.account.entity.transfer.AccountDTO;
import com.kuretru.web.libra.account.service.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "账户")
public class AccountController extends BaseRestController<AccountService, AccountDTO, AccountQuery> {

    @Autowired
    public AccountController(AccountService service) {
        super(service);
    }

}
