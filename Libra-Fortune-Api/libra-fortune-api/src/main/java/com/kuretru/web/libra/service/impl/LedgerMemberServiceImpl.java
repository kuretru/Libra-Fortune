package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.authentication.context.AccessTokenContext;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.entity.PaginationQuery;
import com.kuretru.microservices.web.entity.PaginationResponse;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.LedgerMemberDO;
import com.kuretru.web.libra.entity.query.LedgerMemberQuery;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.entity.transfer.LedgerMemberDTO;
import com.kuretru.web.libra.mapper.LedgerMemberMapper;
import com.kuretru.web.libra.service.LedgerMemberService;
import com.kuretru.web.libra.service.LedgerService;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 账本成员管理
 * <p>
 * 只能查看与自己有关联的账本的成员
 * 只有账本的拥有者可以修改账本成员
 *
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Service
public class LedgerMemberServiceImpl extends BaseServiceImpl<LedgerMemberMapper, LedgerMemberDO, LedgerMemberDTO, LedgerMemberQuery> implements LedgerMemberService {

    private final LedgerService ledgerService;

    @Autowired
    public LedgerMemberServiceImpl(LedgerMemberMapper mapper, LedgerMemberEntityMapper entityMapper,
                                   @Lazy LedgerService ledgerService) {
        super(mapper, entityMapper);
        this.ledgerService = ledgerService;
    }

    @Override
    public LedgerMemberDTO get(UUID uuid) throws ServiceException {
        LedgerMemberDTO result = super.get(uuid);
        if (result != null) {
            UUID myUserId = AccessTokenContext.getUserId();
            if (!myUserId.equals(result.getUserId())) {
                throw ServiceException.build(UserErrorCodes.ACCESS_PERMISSION_ERROR, "仅能查看自己信息");
            }
        }
        return result;
    }

    @Override
    protected List<LedgerMemberDTO> list(QueryWrapper<LedgerMemberDO> queryWrapper) {
        List<LedgerMemberDTO> result = super.list(queryWrapper);
        verifyListResult(result);
        return result;
    }

    @Override
    protected PaginationResponse<LedgerMemberDTO> list(PaginationQuery pagination, QueryWrapper<LedgerMemberDO> queryWrapper) {
        PaginationResponse<LedgerMemberDTO> result = super.list(pagination, queryWrapper);
        verifyListResult(result.getList());
        return result;
    }

    private void verifyListResult(List<LedgerMemberDTO> records) throws ServiceException {
        UUID myUserId = AccessTokenContext.getUserId();
        for (LedgerMemberDTO record : records) {
            if (myUserId.equals(record.getUserId())) {
                return;
            }
        }
        throw ServiceException.build(UserErrorCodes.ACCESS_PERMISSION_ERROR, "仅能查看自己所在账本的账本成员");
    }

    @Override
    public void remove(UUID uuid) throws ServiceException {
        LedgerMemberDTO ledgerMemberDTO = this.get(uuid);
        if (ledgerMemberDTO == null) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "指定账本成员不存在");
        }

        UUID myUserId = AccessTokenContext.getUserId();
        // 1. 自己可以主动退出某个账本
        // 2. 否则只有账本拥有者可以踢出某个成员
        if (!ledgerMemberDTO.getUserId().equals(myUserId)) {
            verifyDTO(ledgerMemberDTO);
        }
        super.remove(uuid);
    }

    @Override
    protected void verifyDTO(LedgerMemberDTO record) throws ServiceException {
        LedgerDTO ledgerDTO = ledgerService.get(record.getLedgerId());
        if (ledgerDTO == null) {
            throw ServiceException.build(UserErrorCodes.REQUEST_PARAMETER_ERROR, "指定账本不存在");
        }
        UUID myUserId = AccessTokenContext.getUserId();
        if (!ledgerDTO.getOwnerId().equals(myUserId)) {
            throw ServiceException.build(UserErrorCodes.ACCESS_PERMISSION_ERROR, "账本拥有者才可以修改账本成员");
        }
    }

    @Mapper(componentModel = "spring")
    interface LedgerMemberEntityMapper extends BaseServiceImpl.BaseEntityMapper<LedgerMemberDO, LedgerMemberDTO> {

    }

}
