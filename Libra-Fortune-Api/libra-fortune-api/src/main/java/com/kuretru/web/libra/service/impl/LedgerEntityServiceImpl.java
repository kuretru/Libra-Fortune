package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerCategoryDO;
import com.kuretru.web.libra.entity.data.LedgerEntityDO;
import com.kuretru.web.libra.entity.query.LedgerEntityQuery;
import com.kuretru.web.libra.entity.transfer.LedgerEntityDTO;
import com.kuretru.web.libra.mapper.LedgerEntityMapper;
import com.kuretru.web.libra.service.LedgerCategoryService;
import com.kuretru.web.libra.service.LedgerEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LedgerEntityServiceImpl extends BaseServiceImpl<LedgerEntityMapper, LedgerEntityDO, LedgerEntityDTO, LedgerEntityQuery> implements LedgerEntityService {
    private final LedgerCategoryService ledgerCategoryService;

    @Autowired
    public LedgerEntityServiceImpl(LedgerEntityMapper mapper, LedgerCategoryService ledgerCategoryService) {
        super(mapper, LedgerEntityDO.class, LedgerEntityDTO.class, LedgerEntityQuery.class);
        this.ledgerCategoryService = ledgerCategoryService;
    }

    @Override
    public synchronized LedgerEntityDTO save(LedgerEntityDTO record) throws ServiceException {
//        查询categoryid 是不是在ledgerid下有
        QueryWrapper<LedgerCategoryDO> queryWrapper = new QueryWrapper<>();
//        column 是表的列
        queryWrapper.eq("uuid", record.getCategoryId());
        queryWrapper.eq("ledger_id", record.getLedgerId());
//        if (ledgerCategoryService.(queryWrapper) == null) {
//            throw new ServiceException.InternalServerError(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "该类型不属于该账本，不可操作");
//        }
        record.setAmount(record.getAmount() * 10000);
        return super.save(record);
    }

    @Override
    protected LedgerEntityDTO doToDto(LedgerEntityDO record) {
        LedgerEntityDTO result = super.doToDto(record);
        if (result != null) {
            result.setLedgerId(UUID.fromString(record.getLedgerId()));
            result.setCategoryId(UUID.fromString(record.getCategoryId()));
        }
        return result;
    }

    @Override
    protected LedgerEntityDO dtoToDo(LedgerEntityDTO record) {
        LedgerEntityDO result = super.dtoToDo(record);
        if (result != null) {
            result.setLedgerId(record.getLedgerId().toString());
            result.setCategoryId(record.getCategoryId().toString());
        }
        return result;
    }


}
