package com.kuretru.web.libra.ledger.service.impl;

import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.v2.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.ledger.entity.data.LedgerEntryDO;
import com.kuretru.web.libra.ledger.entity.mapper.LedgerEntryEntityMapper;
import com.kuretru.web.libra.ledger.entity.query.LedgerEntryQuery;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerEntryDTO;
import com.kuretru.web.libra.ledger.mapper.LedgerEntryMapper;
import com.kuretru.web.libra.ledger.service.LedgerEntryDetailService;
import com.kuretru.web.libra.ledger.service.LedgerEntryService;
import com.kuretru.web.libra.ledger.service.LedgerEntryTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ledgerV2EntryService")
public class LedgerEntryServiceImpl
        extends BaseServiceImpl<LedgerEntryMapper, LedgerEntryDO, LedgerEntryDTO, LedgerEntryQuery>
        implements LedgerEntryService {

    private final LedgerEntryDetailService detailService;
    private final LedgerEntryTagService tagService;

    @Autowired
    public LedgerEntryServiceImpl(LedgerEntryMapper mapper, LedgerEntryEntityMapper entityMapper,
                                  LedgerEntryTagService tagService, LedgerEntryDetailService detailService) {
        super(mapper, entityMapper);
        this.tagService = tagService;
        this.detailService = detailService;
    }

    @Override
    protected LedgerEntryDTO afterGet(LedgerEntryDO record) throws ServiceException {
        var result = super.afterGet(record);
        result.setTags(tagService.listByParentId(record.getId()));
        result.setDetails(detailService.listByParentId(record.getId()));
        return result;
    }

    @Override
    protected LedgerEntryDTO afterSave(LedgerEntryDO record, LedgerEntryDTO raw) throws ServiceException {
        var result = super.afterSave(record, raw);
        result.setTags(tagService.syncByParentId(record.getId(), raw.getTags()));
        result.setDetails(detailService.syncByParentId(record.getId(), raw.getDetails()));
        return result;
    }

    @Override
    protected LedgerEntryDTO afterUpdate(LedgerEntryDO record, LedgerEntryDTO raw) throws ServiceException {
        var result = super.afterUpdate(record, raw);
        result.setTags(tagService.listByParentId(record.getId()));
        result.setDetails(detailService.listByParentId(record.getId()));
        return result;
    }

    @Override
    protected void afterRemove(LedgerEntryDO record) throws ServiceException {
        tagService.removeByParentId(record.getId());
        detailService.removeByParentId(record.getId());
    }

}
