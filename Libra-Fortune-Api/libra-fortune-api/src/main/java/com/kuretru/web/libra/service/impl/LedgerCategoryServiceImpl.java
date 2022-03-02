package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.api.common.constant.code.ServiceErrorCodes;
import com.kuretru.api.common.exception.ServiceException;
import com.kuretru.api.common.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerCategoryDO;
import com.kuretru.web.libra.entity.data.LedgerDO;
import com.kuretru.web.libra.entity.data.SysUserDO;
import com.kuretru.web.libra.entity.query.LedgerCategoryQuery;
import com.kuretru.web.libra.entity.query.LedgerQuery;
import com.kuretru.web.libra.entity.transfer.LedgerCategoryDTO;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.entity.transfer.SysUserDTO;
import com.kuretru.web.libra.mapper.LedgerCategoryMapper;
import com.kuretru.web.libra.service.LedgerCategoryService;
import com.kuretru.web.libra.service.LedgerService;
import com.kuretru.web.libra.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LedgerCategoryServiceImpl extends BaseServiceImpl<LedgerCategoryMapper, LedgerCategoryDO, LedgerCategoryDTO, LedgerCategoryQuery> implements LedgerCategoryService {
    private final LedgerService ledgerService;
    private final SysUserService userService;

    @Autowired
    public LedgerCategoryServiceImpl(LedgerCategoryMapper mapper, LedgerService ledgerService, SysUserService userService) {
        super(mapper, LedgerCategoryDO.class, LedgerCategoryDTO.class);
        this.ledgerService = ledgerService;
        this.userService = userService;
    }

    @Override
    public synchronized LedgerCategoryDTO save(LedgerCategoryDTO record) throws ServiceException {
        String ledgerId = record.getUserId();
        if (ledgerService.get(UUID.fromString(ledgerId)) == null) {
            throw new ServiceException.InternalServerError(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "该账本不存在，不可操作");
        }
        QueryWrapper<LedgerCategoryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", record.getName());
//        column 是表的列
        queryWrapper.eq("ledger_id", record.getUserId());
        if (get(queryWrapper) != null) {
            throw new ServiceException.InternalServerError(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "产生了已存在的类，请重新修改名称后提交");
        }
        return super.save(record);
    }

    @Override
    public LedgerCategoryDTO get(QueryWrapper<LedgerCategoryDO> queryWrapper) {
        LedgerCategoryDO record = mapper.selectOne(queryWrapper);
        return doToDto(record);
    }


}
