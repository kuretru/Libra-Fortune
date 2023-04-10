package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.web.constant.code.ServiceErrorCodes;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerEntryCategoryDO;
import com.kuretru.web.libra.entity.query.LedgerEntryCategoryQuery;
import com.kuretru.web.libra.entity.transfer.LedgerEntryCategoryDTO;
import com.kuretru.web.libra.mapper.LedgerEntryCategoryMapper;
import com.kuretru.web.libra.service.LedgerEntryCategoryService;
import com.kuretru.web.libra.service.LedgerService;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Service
public class LedgerEntryCategoryServiceImpl extends BaseServiceImpl<LedgerEntryCategoryMapper, LedgerEntryCategoryDO, LedgerEntryCategoryDTO, LedgerEntryCategoryQuery> implements LedgerEntryCategoryService {

    private final LedgerService ledgerService;

    @Autowired
    public LedgerEntryCategoryServiceImpl(LedgerEntryCategoryMapper mapper, LedgerEntryCategoryEntityMapper entityMapper, @Lazy LedgerService ledgerService) {
        super(mapper, entityMapper);
        this.ledgerService = ledgerService;
    }

    @Override
    public LedgerEntryCategoryDTO save(LedgerEntryCategoryDTO record) throws ServiceException {
        QueryWrapper<LedgerEntryCategoryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ledger_id", record.getLedgerId().toString());
        queryWrapper.eq("name", record.getName());
        if (!list(queryWrapper).isEmpty()) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "该账本下已存在同名分类");
        }
        return super.save(record);
    }

    @Override
    public LedgerEntryCategoryDTO update(LedgerEntryCategoryDTO record) throws ServiceException {
        LedgerEntryCategoryDTO old = get(record.getId());
        if (!old.getLedgerId().equals(record.getLedgerId())) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "无法修改账本ID");
        }
        return super.update(record);
    }

    @Override
    public void remove(UUID uuid) throws ServiceException {
        throw ServiceException.build(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "尚未实现");
        // TODO: 验证该分类下是否已不存在条目
        // super.remove(uuid);
    }

    @Override
    protected void verifyDTO(LedgerEntryCategoryDTO record) throws ServiceException {
        ledgerService.verifyIamLedgerOwner(record.getLedgerId());
    }

    @Mapper(componentModel = "spring")
    interface LedgerEntryCategoryEntityMapper extends BaseServiceImpl.BaseEntityMapper<LedgerEntryCategoryDO, LedgerEntryCategoryDTO> {

    }

}
