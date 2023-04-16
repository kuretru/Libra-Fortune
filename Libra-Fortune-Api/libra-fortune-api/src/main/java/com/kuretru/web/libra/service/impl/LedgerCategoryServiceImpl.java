package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.web.constant.code.ServiceErrorCodes;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerCategoryDO;
import com.kuretru.web.libra.entity.query.LedgerCategoryQuery;
import com.kuretru.web.libra.entity.transfer.LedgerCategoryDTO;
import com.kuretru.web.libra.mapper.LedgerCategoryMapper;
import com.kuretru.web.libra.service.LedgerCategoryService;
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
public class LedgerCategoryServiceImpl
        extends BaseServiceImpl<LedgerCategoryMapper, LedgerCategoryDO, LedgerCategoryDTO, LedgerCategoryQuery>
        implements LedgerCategoryService {

    private final LedgerService ledgerService;

    @Autowired
    public LedgerCategoryServiceImpl(LedgerCategoryMapper mapper, LedgerEntryCategoryEntityMapper entityMapper,
                                     @Lazy LedgerService ledgerService) {
        super(mapper, entityMapper);
        this.ledgerService = ledgerService;
    }

    @Override
    public LedgerCategoryDTO update(LedgerCategoryDTO record) throws ServiceException {
        LedgerCategoryDTO old = get(record.getId());
        if (!old.getLedgerId().equals(record.getLedgerId())) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "无法修改账本ID");
        }
        return super.update(record);
    }

    @Override
    public void remove(UUID uuid) throws ServiceException {
        throw new ServiceException(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "尚未实现");
        // TODO: 验证该分类下是否已不存在条目
        // super.remove(uuid);
    }

    @Override
    protected void verifyDTO(LedgerCategoryDTO record) throws ServiceException {
        ledgerService.verifyIamLedgerOwner(record.getLedgerId());
    }

    @Override
    protected LedgerCategoryDTO findUniqueRecord(LedgerCategoryDTO record) {
        QueryWrapper<LedgerCategoryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ledger_id", record.getLedgerId().toString());
        queryWrapper.eq("name", record.getName());
        LedgerCategoryDO result = mapper.selectOne(queryWrapper);
        return entityMapper.doToDto(result);
    }

    @Mapper(componentModel = "spring")
    interface LedgerEntryCategoryEntityMapper extends BaseServiceImpl.BaseEntityMapper<LedgerCategoryDO, LedgerCategoryDTO> {

    }

}
