package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.api.common.constant.code.UserErrorCodes;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerCategoryDO;
import com.kuretru.web.libra.entity.query.LedgerCategoryQuery;
import com.kuretru.web.libra.entity.transfer.LedgerCategoryDTO;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.mapper.LedgerCategoryMapper;
import com.kuretru.web.libra.service.LedgerCategoryService;
import com.kuretru.web.libra.service.LedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LedgerCategoryServiceImpl extends BaseServiceImpl<LedgerCategoryMapper, LedgerCategoryDO, LedgerCategoryDTO, LedgerCategoryQuery> implements LedgerCategoryService {

    private final LedgerService ledgerService;

    @Autowired
    public LedgerCategoryServiceImpl(LedgerCategoryMapper mapper, LedgerService ledgerService) {
        super(mapper, LedgerCategoryDO.class, LedgerCategoryDTO.class);
        this.ledgerService = ledgerService;
    }

    @Override
    protected LedgerCategoryDTO doToDto(LedgerCategoryDO record) {
        LedgerCategoryDTO result = super.doToDto(record);
        if (result != null) {
            result.setLedgerId(UUID.fromString(record.getLedgerId()));
        }
        return result;
    }

    @Override
    protected LedgerCategoryDO dtoToDo(LedgerCategoryDTO record) {
        LedgerCategoryDO result = super.dtoToDo(record);
        if (result != null) {
            result.setLedgerId(record.getLedgerId().toString());
        }
        return result;
    }

    @Override
    protected void verifyDTO(LedgerCategoryDTO record) throws ServiceException {
        LedgerDTO ledgerDTO = ledgerService.get(record.getLedgerId());
        if (ledgerDTO == null) {
            throw new ServiceException.BadRequest(UserErrorCodes.REQUEST_PARAMETER_ERROR, "该账本不存在");
        }
        QueryWrapper<LedgerCategoryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ledger_id", record.getLedgerId().toString());
        queryWrapper.eq("name", record.getName());
        LedgerCategoryDO one = mapper.selectOne(queryWrapper);
        if (one != null) {
            throw new ServiceException.BadRequest(UserErrorCodes.USER_REPEATED_REQUEST, "该账本下已存在该分类");
        }
    }

}
