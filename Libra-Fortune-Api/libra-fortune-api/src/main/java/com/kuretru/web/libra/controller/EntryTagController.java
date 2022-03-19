package com.kuretru.web.libra.controller;

import com.kuretru.api.common.controller.BaseRestController;
import com.kuretru.web.libra.entity.query.EntryTagQuery;
import com.kuretru.web.libra.entity.transfer.EntryTagDTO;
import com.kuretru.web.libra.service.EntryTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/entries/{entryId}/tags/{tagId}")
public class EntryTagController extends BaseRestController<EntryTagService, EntryTagDTO, EntryTagQuery> {

    @Autowired
    public EntryTagController(EntryTagService service) {
        super(service);
    }

}