package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.entity.PaginationQuery;
import com.kuretru.microservices.web.entity.PaginationResponse;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerCategoryDO;
import com.kuretru.web.libra.entity.mapper.LedgerCategoryEntityMapper;
import com.kuretru.web.libra.entity.query.LedgerCategoryQuery;
import com.kuretru.web.libra.entity.transfer.LedgerCategoryDTO;
import com.kuretru.web.libra.mapper.LedgerCategoryMapper;
import com.kuretru.web.libra.service.LedgerCategoryService;
import com.kuretru.web.libra.service.LedgerEntryService;
import com.kuretru.web.libra.service.LedgerMemberService;
import com.kuretru.web.libra.service.LedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Service
public class LedgerCategoryServiceImpl
        extends BaseServiceImpl<LedgerCategoryMapper, LedgerCategoryDO, LedgerCategoryDTO, LedgerCategoryQuery>
        implements LedgerCategoryService {

    private final LedgerService ledgerService;
    private final LedgerMemberService memberService;
    private final LedgerEntryService entryService;

    @Autowired
    public LedgerCategoryServiceImpl(LedgerCategoryMapper mapper, LedgerCategoryEntityMapper entityMapper,
                                     @Lazy LedgerService ledgerService, @Lazy LedgerMemberService memberService,
                                     @Lazy LedgerEntryService entryService) {
        super(mapper, entityMapper);
        this.ledgerService = ledgerService;
        this.memberService = memberService;
        this.entryService = entryService;
    }

    @Override
    public LedgerCategoryDTO getDirect(UUID uuid) {
        LedgerCategoryDO record = getDO(uuid);
        return entityMapper.doToDto(record);
    }

    @Override
    public List<LedgerCategoryDTO> list() {
        throw new ServiceException(UserErrorCodes.MISSING_REQUIRED_PARAMETERS, "账本分类必须关联账本ID查询");
    }

    @Override
    public PaginationResponse<LedgerCategoryDTO> list(PaginationQuery paginationQuery) {
        throw new ServiceException(UserErrorCodes.MISSING_REQUIRED_PARAMETERS, "账本分类必须关联账本ID查询");
    }

    @Override
    public LedgerCategoryDTO update(LedgerCategoryDTO record) throws ServiceException {
        LedgerCategoryDO old = getDO(record.getId());
        if (!UUID.fromString(old.getLedgerId()).equals(record.getLedgerId())) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "无法修改账本ID");
        }
        return super.update(record);
    }

    @Override
    protected void verifyCanGet(LedgerCategoryDO record) throws ServiceException {
        // 只有该账本的成员可以查看该账本的分类
        memberService.verifyIamLedgerMember(UUID.fromString(record.getLedgerId()));
    }

    @Override
    protected void verifyCanRemove(LedgerCategoryDO record) throws ServiceException {
        // 只有该账本的拥有者可以删除该账本的分类
        ledgerService.verifyIamLedgerOwner(UUID.fromString(record.getLedgerId()));
        long count = entryService.countByCategoryId(UUID.fromString(record.getUuid()));
        if (count != 0) {
            String message = String.format("该分类下存在%d个条目，无法删除", count);
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, message);
        }
    }

    @Override
    protected void verifyQuery(LedgerCategoryQuery query) throws ServiceException {
        // 只有该账本的成员可以查看该账本的分类
        memberService.verifyIamLedgerMember(query.getLedgerId());
    }

    @Override
    protected void verifyDTO(LedgerCategoryDTO record) throws ServiceException {
        // 只有该账本的拥有者可以修改该账本的分类
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

}
