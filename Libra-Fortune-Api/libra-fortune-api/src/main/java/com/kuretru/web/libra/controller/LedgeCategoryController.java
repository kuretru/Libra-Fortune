package com.kuretru.web.libra.controller;

import com.kuretru.api.common.constant.EmptyConstants;
import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.controller.BaseRestController;
import com.kuretru.api.common.entity.ApiResponse;
import com.kuretru.api.common.entity.PaginationQuery;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.web.libra.entity.query.LedgerCategoryQuery;
import com.kuretru.web.libra.entity.query.LedgerQuery;
import com.kuretru.web.libra.entity.transfer.LedgerCategoryDTO;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.service.LedgerCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/categories")
public class LedgeCategoryController extends BaseRestController<LedgerCategoryService, LedgerCategoryDTO, LedgerCategoryQuery> {

    @Autowired
    public LedgeCategoryController(LedgerCategoryService service) {
        super(service);
    }

}
