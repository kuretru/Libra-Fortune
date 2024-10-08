package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuretru.microservices.authentication.context.AccessTokenContext;
import com.kuretru.microservices.web.constant.code.ServiceErrorCodes;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.entity.PaginationQuery;
import com.kuretru.microservices.web.entity.PaginationResponse;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.business.LedgerBO;
import com.kuretru.web.libra.entity.data.LedgerDO;
import com.kuretru.web.libra.entity.mapper.LedgerEntityMapper;
import com.kuretru.web.libra.entity.query.LedgerQuery;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.entity.transfer.LedgerMemberDTO;
import com.kuretru.web.libra.entity.view.LedgerVO;
import com.kuretru.web.libra.mapper.LedgerMapper;
import com.kuretru.web.libra.service.LedgerMemberService;
import com.kuretru.web.libra.service.LedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Service
public class LedgerServiceImpl
        extends BaseServiceImpl<LedgerMapper, LedgerDO, LedgerDTO, LedgerQuery>
        implements LedgerService {

    private final LedgerMemberService ledgerMemberService;

    @Autowired
    public LedgerServiceImpl(LedgerMapper mapper, LedgerEntityMapper entityMapper,
                             @Lazy LedgerMemberService ledgerMemberService) {
        super(mapper, entityMapper);
        this.ledgerMemberService = ledgerMemberService;
    }

    @Override
    public LedgerDTO getDirect(UUID uuid) {
        LedgerDO record = getDO(uuid);
        return entityMapper.doToDto(record);
    }

    @Override
    public PaginationResponse<LedgerVO> listVo(PaginationQuery pagination, LedgerQuery query) {
        QueryWrapper<LedgerBO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ledger_member.user_id", AccessTokenContext.getUserId().toString());
        if (query.getOwnerId() != null) {
            queryWrapper.eq("ledger.owner_id", query.getOwnerId().toString());
        }
        if (query.getName() != null) {
            queryWrapper.like("ledger.name", query.getName());
        }
        if (query.getType() != null) {
            queryWrapper.eq("ledger.type", query.getType().getCode());
        }
        queryWrapper.orderByAsc("id");

        IPage<LedgerBO> page = new Page<>(pagination.getCurrent(), pagination.getPageSize());
        page = mapper.listPageBo(page, queryWrapper);
        List<LedgerVO> records = ((LedgerEntityMapper) entityMapper).boToVo(page.getRecords());
        return new PaginationResponse<>(records, page.getCurrent(), page.getSize(), page.getTotal());
    }

    @Override
    public void verifyIamLedgerOwner(UUID ledgerId) throws ServiceException {
        LedgerDO record = getDO(ledgerId);
        if (record == null) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "指定账本不存在");
        }
        UUID myUserId = AccessTokenContext.getUserId();
        boolean iAmNowOwner = !UUID.fromString(record.getOwnerId()).equals(myUserId);
        if (iAmNowOwner) {
            throw ServiceException.build(UserErrorCodes.ACCESS_PERMISSION_ERROR, "非账本拥有者");
        }
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = ServiceException.class)
    public LedgerDTO save(LedgerDTO record) throws ServiceException {
        boolean iAmNotOwner = !record.getOwnerId().equals(AccessTokenContext.getUserId());
        if (iAmNotOwner) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "账本所属用户必须为当前登录用户");
        }
        LedgerDTO result = super.save(record);

        LedgerMemberDTO ledgerMemberDTO = new LedgerMemberDTO();
        ledgerMemberDTO.setLedgerId(result.getId());
        ledgerMemberDTO.setUserId(AccessTokenContext.getUserId());
        ledgerMemberDTO.setDefaultFundedRatio((short) 100);
        ledgerMemberService.save(ledgerMemberDTO);

        return result;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = ServiceException.class)
    public LedgerDTO update(LedgerDTO record) throws ServiceException {
        LedgerDTO old = entityMapper.doToDto(getDO(record.getId()));
        if (old == null) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "该账本不存在");
        } else if (!old.getOwnerId().equals(AccessTokenContext.getUserId())) {
            throw new ServiceException(UserErrorCodes.ACCESS_UNAUTHORIZED, "只有账本拥有者可以修改账本");
        } else if (!record.getOwnerId().equals(old.getOwnerId())) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "暂不可修改账本拥有者");
        } else if (!record.getType().equals(old.getType())) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "暂不可修改账本类型");
        }
        return super.update(record);
    }

    @Override
    public void remove(UUID uuid) throws ServiceException {
        throw ServiceException.build(ServiceErrorCodes.SYSTEM_EXECUTION_ERROR, "尚未实现");
    }

    @Override
    protected void verifyCanGet(LedgerDO record) throws ServiceException {
        ledgerMemberService.verifyIamLedgerMember(UUID.fromString(record.getUuid()));
    }

    @Override
    protected LedgerDTO findUniqueRecord(LedgerDTO record) {
        QueryWrapper<LedgerDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner_id", record.getOwnerId().toString());
        queryWrapper.eq("name", record.getName());
        LedgerDO result = mapper.selectOne(queryWrapper);
        return entityMapper.doToDto(result);
    }

}
