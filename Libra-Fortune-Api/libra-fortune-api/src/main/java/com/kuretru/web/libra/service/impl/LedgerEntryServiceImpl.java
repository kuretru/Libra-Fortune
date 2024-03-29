package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuretru.microservices.authentication.context.AccessTokenContext;
import com.kuretru.microservices.common.utils.UuidUtils;
import com.kuretru.microservices.web.constant.code.ServiceErrorCodes;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.entity.PaginationQuery;
import com.kuretru.microservices.web.entity.PaginationResponse;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.business.LedgerEntryBO;
import com.kuretru.web.libra.entity.business.LedgerEntryDetailBO;
import com.kuretru.web.libra.entity.business.LedgerEntryDetailTagBO;
import com.kuretru.web.libra.entity.data.LedgerEntryDO;
import com.kuretru.web.libra.entity.enums.LedgerTypeEnum;
import com.kuretru.web.libra.entity.mapper.LedgerEntryEntityMapper;
import com.kuretru.web.libra.entity.query.LedgerEntryQuery;
import com.kuretru.web.libra.entity.transfer.LedgerCategoryDTO;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.entity.transfer.LedgerEntryDTO;
import com.kuretru.web.libra.entity.transfer.LedgerEntryDetailDTO;
import com.kuretru.web.libra.entity.view.*;
import com.kuretru.web.libra.mapper.LedgerEntryMapper;
import com.kuretru.web.libra.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Service
public class LedgerEntryServiceImpl
        extends BaseServiceImpl<LedgerEntryMapper, LedgerEntryDO, LedgerEntryDTO, LedgerEntryQuery>
        implements LedgerEntryService {

    private final LedgerService ledgerService;
    private final LedgerCategoryService categoryService;
    private final LedgerMemberService memberService;
    private final LedgerTagService tagService;
    private final LedgerEntryDetailService entryDetailService;
    private final PaymentChannelService paymentChannelService;

    @Autowired
    public LedgerEntryServiceImpl(LedgerEntryMapper mapper, LedgerEntryEntityMapper entityMapper,
                                  @Lazy LedgerService ledgerService, @Lazy LedgerCategoryService categoryService,
                                  @Lazy LedgerMemberService memberService, @Lazy LedgerTagService tagService,
                                  @Lazy LedgerEntryDetailService entryDetailService, @Lazy PaymentChannelService paymentChannelService) {
        super(mapper, entityMapper);
        this.entryDetailService = entryDetailService;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.memberService = memberService;
        this.ledgerService = ledgerService;
        this.paymentChannelService = paymentChannelService;
    }

    @Override
    public PaginationResponse<LedgerEntryVO> listVo(PaginationQuery pagination, LedgerEntryQuery query) {
        QueryWrapper<LedgerEntryBO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ledger_entry.ledger_id", query.getLedgerId().toString());
        if (query.getCategoryId() != null) {
            queryWrapper.eq("ledger_entry.category_id", query.getCategoryId().toString());
        }
        if (query.getDate() != null) {
            queryWrapper.eq("ledger_entry.date", query.getDate());
        }
        if (query.getName() != null) {
            queryWrapper.like("ledger_entry.name", query.getName());
        }
        if (query.getCurrencyType() != null) {
            queryWrapper.eq("ledger_entry.currency_type", query.getCurrencyType());
        }
        queryWrapper.orderByDesc("date");
        queryWrapper.orderByDesc("id");

        IPage<LedgerEntryBO> page = new Page<>(pagination.getCurrent(), pagination.getPageSize());
        page = mapper.listPageBo(page, queryWrapper);

        List<LedgerEntryBO> boList = page.getRecords();
        List<LedgerEntryVO> voList = ((LedgerEntryEntityMapper)entityMapper).boToVo(boList);
        Map<UUID, LedgerCategoryVO> categoryMap = categoryService.listMapByLedgerId(query.getLedgerId());
        Map<UUID, UserVO> userMap = memberService.listUserMapByLedgerId(query.getLedgerId());
        Map<UUID, PaymentChannelVO> paymentChannelMap = paymentChannelService.listMapByLedgerId(query.getLedgerId());
        Map<UUID, LedgerTagVO> myTagMap = tagService.listMyLedgerTagsMapVO();

        for (int i = 0; i < boList.size(); i++) {
            LedgerEntryBO entryBO = boList.get(i);
            LedgerEntryVO entryVO = voList.get(i);

            UUID categoryId = UUID.fromString(entryBO.getCategoryId());
            if (!categoryMap.containsKey(categoryId)) {
                throw new ServiceException(ServiceErrorCodes.SYSTEM_NOT_IMPLEMENTED, "不存在的分类ID");
            }
            entryVO.setCategory(categoryMap.get(categoryId));

            for (int j = 0; j < entryBO.getDetails().size(); j++) {
                LedgerEntryDetailBO detailBO = entryBO.getDetails().get(j);
                LedgerEntryDetailVO detailVO = entryVO.getDetails().get(j);

                UUID userId = UUID.fromString(detailBO.getUserId());
                if (!userMap.containsKey(userId)) {
                    throw new ServiceException(ServiceErrorCodes.SYSTEM_NOT_IMPLEMENTED, "不存在的用户ID");
                }
                detailVO.setUser(userMap.get(userId));

                UUID paymentChannelId = UUID.fromString(detailBO.getPaymentChannelId());
                if (!paymentChannelMap.containsKey(paymentChannelId)) {
                    throw new ServiceException(ServiceErrorCodes.SYSTEM_NOT_IMPLEMENTED, "不存在的支出渠道ID");
                }
                detailVO.setPaymentChannel(paymentChannelMap.get(paymentChannelId));

                boolean isMyLedgerDetail = UUID.fromString(detailBO.getUserId()).equals(AccessTokenContext.getUserId());
                boolean hasTags = detailBO.getTags() != null && !detailBO.getTags().isEmpty();
                if (isMyLedgerDetail && hasTags) {
                    List<LedgerTagVO> tagVo = new ArrayList<>(detailBO.getTags().size());
                    for (int k = 0; k < detailBO.getTags().size(); k++) {
                        LedgerEntryDetailTagBO tagBo = detailBO.getTags().get(k);

                        UUID tagId = UUID.fromString(tagBo.getTagId());
                        if (!myTagMap.containsKey(tagId)) {
                            throw new ServiceException(ServiceErrorCodes.SYSTEM_NOT_IMPLEMENTED, "不存在的标签ID");
                        }
                        tagVo.add(myTagMap.get(tagId));
                    }
                    detailVO.setTags(tagVo);
                }
            }
        }

        // TODO: 排序，Owner应该在最前
        return new PaginationResponse<>(voList, page.getCurrent(), page.getSize(), page.getTotal());
    }

    @Override
    public long countByCategoryId(UUID categoryId) {
        QueryWrapper<LedgerEntryDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId.toString());
        return mapper.selectCount(queryWrapper);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = ServiceException.class)
    public LedgerEntryDTO save(LedgerEntryDTO record) throws ServiceException {
        LedgerEntryDTO result = super.save(record);
        record.getDetails().forEach(detail -> detail.setEntryId(result.getId()));
        List<LedgerEntryDetailDTO> detailResult = entryDetailService.save(result.getId(), record.getDetails());
        result.setDetails(detailResult);
        return result;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = ServiceException.class)
    public LedgerEntryDTO update(LedgerEntryDTO record) throws ServiceException {
        LedgerEntryDTO old = entityMapper.doToDto(getDO(record.getId()));
        if (old == null) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "指定条目不存在");
        } else if (!old.getLedgerId().equals(record.getLedgerId())) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不能修改账本ID");
        }
        LedgerEntryDTO result = super.update(record);

        for (LedgerEntryDetailDTO detail : record.getDetails()) {
            if (UuidUtils.isEmpty(detail.getEntryId())) {
                detail.setEntryId(record.getId());
            } else if (!detail.getEntryId().equals(record.getId())) {
                throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "条目ID错误");
            }
        }

        List<LedgerEntryDetailDTO> detailResult = entryDetailService.update(result.getId(), record.getDetails());
        result.setDetails(detailResult);
        return result;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = ServiceException.class)
    public void remove(UUID uuid) throws ServiceException {
        super.remove(uuid);
        entryDetailService.removeByEntryId(uuid);
    }

    @Override
    protected void verifyCanGet(LedgerEntryDO record) throws ServiceException {
        memberService.verifyIamLedgerMember(UUID.fromString(record.getLedgerId()));
    }

    @Override
    protected void verifyCanRemove(LedgerEntryDO record) throws ServiceException {
        memberService.verifyIamLedgerMember(UUID.fromString(record.getLedgerId()));
    }

    @Override
    protected void verifyDTO(LedgerEntryDTO record) throws ServiceException {
        // 1. 判断账本是否存在
        LedgerDTO ledgerDTO = ledgerService.getDirect(record.getLedgerId());
        if (ledgerDTO == null) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "指定账本不存在");
        }

        // 2. 判断是否为改账本成员
        // TODO: 后期修改为判断是否有权限修改
        memberService.verifyIamLedgerMember(record.getLedgerId());

        // 3. 判断账本分类是否存在且是否属于该账本
        LedgerCategoryDTO categoryDTO = categoryService.getDirect(record.getCategoryId());
        if (categoryDTO == null) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "指定分类不存在");
        } else if (!categoryDTO.getLedgerId().equals(record.getLedgerId())) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "该分类不属于该账本");
        }

        // 4. 日期校验
        if (record.getDate().isAfter(LocalDate.now())) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "不能添加今天之后的条目");
        }

        // 5. 货币类型校验
        // TODO 验证货币类型

        // 6. 明细个数校验
        if (record.getDetails().isEmpty()) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "至少需要存在1则条目明细");
        }
        if (ledgerDTO.getType() == LedgerTypeEnum.COMMON) {
            if (record.getDetails().size() != 1) {
                throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "个人账本有且只能有1条条目明细记录");
            }
        }

        long total = 0;
        short percentage = 0;
        Set<UUID> userIds = new HashSet<>(16);
        for (LedgerEntryDetailDTO detail : record.getDetails()) {
            total += detail.getAmount();
            percentage += detail.getFundedRatio();
            userIds.add(detail.getUserId());
        }

        // 7. 校验总和是否相同
        if (total != record.getTotal()) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "条目金额总和与明细不符");
        } else if (percentage != 10000) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "承担比例总和不为100%");
        }

        // 8. 校验分担人是否重复及合法
        if (userIds.size() != record.getDetails().size()) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "存在相同的分担人");
        }
        memberService.verifyTheyAreLedgerMember(record.getLedgerId(), userIds);
    }

}
