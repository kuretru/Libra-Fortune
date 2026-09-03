package com.kuretru.web.libra.account.controller;

import com.kuretru.microservices.web.controller.BaseRestController;
import com.kuretru.web.libra.account.entity.query.AccountTransferQuery;
import com.kuretru.web.libra.account.entity.transfer.AccountTransferDTO;
import com.kuretru.web.libra.account.service.AccountTransferService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts/transfers")
@Tag(name = "账户-转账记录")
public class AccountTransferController
        extends BaseRestController<AccountTransferService, AccountTransferDTO, AccountTransferQuery> {

    @Autowired
    public AccountTransferController(AccountTransferService service) {
        super(service);
    }

}
