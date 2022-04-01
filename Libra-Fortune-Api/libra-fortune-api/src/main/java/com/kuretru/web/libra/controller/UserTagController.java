package com.kuretru.web.libra.controller;

import com.kuretru.microservices.web.controller.BaseRestController;
import com.kuretru.web.libra.entity.query.UserTagQuery;
import com.kuretru.web.libra.entity.transfer.UserTagDTO;
import com.kuretru.web.libra.service.UserTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/tags")
public class UserTagController extends BaseRestController<UserTagService, UserTagDTO, UserTagQuery> {

    @Autowired
    public UserTagController(UserTagService service) {
        super(service);
    }

}
