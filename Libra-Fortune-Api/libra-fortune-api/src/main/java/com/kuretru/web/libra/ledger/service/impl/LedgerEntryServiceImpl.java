package com.kuretru.web.libra.ledger.service.impl;

import com.kuretru.microservices.common.entity.enums.EnumDTO;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.v2.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.ledger.entity.data.LedgerEntryDO;
import com.kuretru.web.libra.ledger.entity.mapper.LedgerEntryEntityMapper;
import com.kuretru.web.libra.ledger.entity.query.LedgerEntryQuery;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerEntryDTO;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerEntryDetailDTO;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerEntryTagDTO;
import com.kuretru.web.libra.ledger.mapper.LedgerEntryMapper;
import com.kuretru.web.libra.ledger.service.LedgerEntryDetailService;
import com.kuretru.web.libra.ledger.service.LedgerEntryService;
import com.kuretru.web.libra.ledger.service.LedgerEntryTagService;
import com.kuretru.web.libra.ledger.service.LedgerService;
import com.kuretru.web.libra.metadata.service.MetadataCategoryService;
import com.kuretru.web.libra.metadata.service.MetadataCurrencyService;
import com.kuretru.web.libra.metadata.service.MetadataTagSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service("ledgerV2EntryService")
public class LedgerEntryServiceImpl extends BaseServiceImpl<LedgerEntryMapper, LedgerEntryDO, LedgerEntryDTO, LedgerEntryQuery> implements LedgerEntryService {

    private static final BigDecimal HUNDRED = new BigDecimal("100.00");

    private final MetadataCategoryService categoryService;
    private final MetadataCurrencyService currencyService;
    private final MetadataTagSetService tagSetService;
    private final LedgerService ledgerService;
    private final LedgerEntryDetailService detailService;
    private final LedgerEntryTagService tagService;

    @Autowired
    public LedgerEntryServiceImpl(LedgerEntryMapper mapper, LedgerEntryEntityMapper entityMapper,
                                  MetadataCategoryService categoryService, MetadataCurrencyService currencyService, MetadataTagSetService tagSetService,
                                  LedgerService ledgerService, LedgerEntryTagService tagService, LedgerEntryDetailService detailService) {
        super(mapper, entityMapper);
        this.categoryService = categoryService;
        this.currencyService = currencyService;
        this.tagSetService = tagSetService;
        this.ledgerService = ledgerService;
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
    protected List<LedgerEntryDTO> afterList(LedgerEntryQuery query, List<LedgerEntryDO> records) throws ServiceException {
        var result = super.afterList(query, records);
        var idList = result.stream().map(LedgerEntryDTO::getId).toList();
        if (idList.isEmpty()) {
            return result;
        }

        var tagMap = tagService.listByParentId(idList);
        var detailMap = detailService.listByParentId(idList);
        for (var entry : result) {
            entry.setTags(tagMap.get(entry.getId()));
            entry.setDetails(detailMap.get(entry.getId()));
        }
        return result;
    }

    @Override
    protected LedgerEntryDO beforeSave(LedgerEntryDTO record) throws ServiceException {
        ledgerService.verifyCanManagerEntry(record.getLedgerId());
        verifyDTO(record);
        return super.beforeSave(record);
    }

    @Override
    protected LedgerEntryDTO afterSave(LedgerEntryDO record, LedgerEntryDTO raw) throws ServiceException {
        var result = super.afterSave(record, raw);
        result.setTags(tagService.syncByParentId(record.getId(), raw.getTags()));
        result.setDetails(detailService.syncByParentId(record.getId(), raw.getDetails()));
        return result;
    }

    @Override
    protected LedgerEntryDO beforeUpdate(LedgerEntryDTO record) throws ServiceException {
        ledgerService.verifyCanManagerEntry(record.getLedgerId());
        verifyDTO(record);
        return super.beforeUpdate(record);
    }

    @Override
    protected LedgerEntryDTO afterUpdate(LedgerEntryDO record, LedgerEntryDTO raw) throws ServiceException {
        var result = super.afterUpdate(record, raw);
        result.setTags(tagService.syncByParentId(record.getId(), raw.getTags()));
        result.setDetails(detailService.syncByParentId(record.getId(), raw.getDetails()));
        return result;
    }

    @Override
    protected LedgerEntryDO beforeRemove(Long id) throws ServiceException {
        var record = super.beforeRemove(id);
        ledgerService.verifyCanManagerEntry(record.getLedgerId());
        return record;
    }

    @Override
    protected void afterRemove(LedgerEntryDO record) throws ServiceException {
        tagService.removeByParentId(record.getId());
        detailService.removeByParentId(record.getId());
    }

    protected void verifyDTO(LedgerEntryDTO record) throws ServiceException {
        // 校验分类存在
        if (categoryService.get(record.getCategoryId()) == null) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账本分类不存在");
        }

        // 校验日期
        if (record.getDate().isAfter(LocalDate.now())) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不能添加今天之后的条目");
        }

        // 校验货币类型
        var currencies = currencyService.enums().stream().map(EnumDTO::getValue).toList();
        if (!currencies.contains(record.getOriginalCurrency())) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "原始消费货币类型不合法");
        } else if (!currencies.contains(record.getSettlementCurrency())) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "结算货币类型不合法");
        }

        // 校验标签
        var tagSetItemIdList = record.getTags().stream().map(LedgerEntryTagDTO::getTagId).toList();
        tagSetService.verifyTagSetItems(tagSetItemIdList);

        // 校验明细
        verifyDetails(record, record.getDetails());
    }

    private void verifyDetails(LedgerEntryDTO entry, List<LedgerEntryDetailDTO> details) throws ServiceException {
        if (details.isEmpty()) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "条目明细不能为空");
        }
        var sum = new BigDecimal(0);
        var ratioSum = new BigDecimal(0);
        var fundedUsernameSet = new HashSet<String>();
        for (var detail : details) {
            if (entry.getId() != null && detail.getEntryId() != null && !detail.getEntryId().equals(entry.getId())) {
                throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "条目详情不属于该条目");
            }
            var deviation = entry.getSettlementAmount().divide(HUNDRED, 4, RoundingMode.HALF_DOWN).multiply(detail.getFundedRatio()).subtract(detail.getAmount()).abs();
            if (deviation.compareTo(new BigDecimal("0.01")) > 0) {
                throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "分担金额与分担比例对不上");
            }
            if (fundedUsernameSet.contains(detail.getUsername())) {
                throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "分担人重复");
            }
            fundedUsernameSet.add(detail.getUsername());
            sum = sum.add(detail.getAmount());
            ratioSum = ratioSum.add(detail.getFundedRatio());
        }
        if (sum.compareTo(entry.getSettlementAmount()) != 0) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "明细金额总和不等于结算金额");
        } else if (ratioSum.compareTo(HUNDRED) != 0) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "分担比例总和不等于100.00%");
        }
    }

}
