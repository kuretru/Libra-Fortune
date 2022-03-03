package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.api.common.constant.code.ServiceErrorCodes;
import com.kuretru.api.common.entity.ApiResponse;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerCategoryDO;
import com.kuretru.web.libra.entity.data.LedgerEntityTagDO;
import com.kuretru.web.libra.entity.data.LedgerTagDO;
import com.kuretru.web.libra.entity.query.LedgerEntityTagQuery;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.entity.transfer.LedgerEntityDTO;
import com.kuretru.web.libra.entity.transfer.LedgerEntityTagDTO;
import com.kuretru.web.libra.entity.transfer.LedgerTagDTO;
import com.kuretru.web.libra.mapper.LedgerEntityTagMapper;
import com.kuretru.web.libra.service.LedgerEntityService;
import com.kuretru.web.libra.service.LedgerEntityTagService;
import com.kuretru.web.libra.service.LedgerService;
import com.kuretru.web.libra.service.LedgerTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LedgerEntityTagServiceImpl extends BaseServiceImpl<LedgerEntityTagMapper, LedgerEntityTagDO, LedgerEntityTagDTO, LedgerEntityTagQuery> implements LedgerEntityTagService {
    private final LedgerTagService tagService;
    private final LedgerEntityService entityService;
    private final LedgerService ledgerService;

    @Autowired
    public LedgerEntityTagServiceImpl(LedgerEntityTagMapper mapper, LedgerTagService tagService, LedgerEntityService entityService, LedgerService ledgerService) {
        super(mapper, LedgerEntityTagDO.class, LedgerEntityTagDTO.class, LedgerEntityTagQuery.class);
        this.tagService = tagService;
        this.entityService = entityService;
        this.ledgerService = ledgerService;
    }

//    @Override
//    public synchronized LedgerEntityTagDTO save(LedgerEntityTagDTO record) throws ServiceException {
//        LedgerEntityDTO ledgerEntityTagDTO = entityService.get(UUID.fromString(record.getEntityId()));
//        LedgerTagDTO ledgerTagDTO = tagService.get(UUID.fromString(record.getTagId()));
//        if (ledgerEntityTagDTO == null || ledgerTagDTO == null) {
//            throw new ServiceException.InternalServerError(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "该账目或该tag不存在，不可操作");
//        }
////        entityid 存在，tagid 存在   但是 不属于同一用户
//        LedgerDTO ledgerDTO = ledgerService.get(UUID.fromString(ledgerEntityTagDTO.getLedgerId()));
//        if (ledgerDTO == null || !ledgerDTO.getUserId().equals(ledgerTagDTO.getUserId())) {
//            throw new ServiceException.InternalServerError(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "该tag不属于该用户，不可操作");
//        }
////        重复添加
//        QueryWrapper<LedgerEntityTagDO> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("tag_id", record.getTagId());
//        queryWrapper.eq("entity_id", record.getEntityId());
//        if (mapper.selectOne(queryWrapper) != null) {
//            throw new ServiceException.InternalServerError(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "该tag已存在，不可重复添加");
//        }
//        return super.save(record);
//    }


}
