package com.kuretru.web.libra.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.BaseService;
import com.kuretru.web.libra.entity.data.LedgerTagDO;
import com.kuretru.web.libra.entity.query.LedgerEntityQuery;
import com.kuretru.web.libra.entity.query.LedgerTagQuery;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.entity.transfer.LedgerEntityDTO;
import com.kuretru.web.libra.entity.transfer.LedgerTagDTO;


public interface LedgerTagService extends BaseService<LedgerTagDTO, LedgerTagQuery> {
}
