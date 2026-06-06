package com.kuretru.web.libra.ledger.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.context.CurrentUserContext;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.v2.service.impl.BaseSequencedServiceImpl;
import com.kuretru.web.libra.ledger.entity.data.LedgerDO;
import com.kuretru.web.libra.ledger.entity.mapper.LedgerEntityMapper;
import com.kuretru.web.libra.ledger.entity.query.LedgerQuery;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.ledger.mapper.LedgerMapper;
import com.kuretru.web.libra.ledger.service.LedgerMemberService;
import com.kuretru.web.libra.ledger.service.LedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ledgerV2Service")
public class LedgerServiceImpl
        extends BaseSequencedServiceImpl<LedgerMapper, LedgerDO, LedgerDTO, LedgerQuery>
        implements LedgerService {

    private final LedgerMemberService memberService;

    @Autowired
    public LedgerServiceImpl(LedgerMapper mapper, LedgerEntityMapper entityMapper,
                             LedgerMemberService memberService) {
        super(mapper, entityMapper);
        this.memberService = memberService;
    }

    @Override
    public int getMaxSequence(LedgerDTO record) {
        var queryWrapper = new QueryWrapper<LedgerDO>();
        queryWrapper.eq("owner", record.getOwner());
        Integer result = mapper.getMaxSequence(queryWrapper);
        return null == result ? 0 : result;
    }

    @Override
    protected LedgerDO findDuplicateRecord(LedgerDTO record) {
        var queryWrapper = new QueryWrapper<LedgerDO>();
        queryWrapper.eq("owner", record.getOwner());
        queryWrapper.eq("name", record.getName());
        return mapper.selectOne(queryWrapper);
    }


    @Override
    protected LedgerDTO afterGet(LedgerDO record) throws ServiceException {
        var result = super.afterGet(record);
        result.setMembers(memberService.listByParentId(record.getId()));
        return result;
    }

    @Override
    protected LedgerDO beforeSave(LedgerDTO record) throws ServiceException {
        record.setOwner(CurrentUserContext.getUsername());
        return super.beforeSave(record);
    }

    @Override
    protected LedgerDTO afterSave(LedgerDO record, LedgerDTO raw) throws ServiceException {
        var result = super.afterSave(record, raw);
        result.setMembers(memberService.syncByParentId(record.getId(), raw.getMembers()));
        return result;
    }

    @Override
    protected LedgerDO beforeUpdate(LedgerDTO record) throws ServiceException {
        LedgerDO oldRecord = mapper.selectById(record.getId());
        verifyOwner(oldRecord);
        record.setOwner(oldRecord.getOwner());
        return super.beforeUpdate(record);
    }

    @Override
    protected LedgerDTO afterUpdate(LedgerDO record, LedgerDTO raw) throws ServiceException {
        var result = super.afterUpdate(record, raw);
        result.setMembers(memberService.syncByParentId(record.getId(), raw.getMembers()));
        return result;
    }

    @Override
    protected LedgerDO beforeRemove(Long id) throws ServiceException {
        LedgerDO oldRecord = mapper.selectById(id);
        verifyOwner(oldRecord);
        return oldRecord;
    }

    @Override
    protected void afterRemove(LedgerDO record) throws ServiceException {
        memberService.removeByParentId(record.getId());
    }

    private void verifyOwner(LedgerDO record) {
        if (!CurrentUserContext.getUsername().equals(record.getOwner())) {
            throw ServiceException.build(UserErrorCodes.ACCESS_PERMISSION_ERROR, "仅账本Owner可以操作");
        }
    }

}
