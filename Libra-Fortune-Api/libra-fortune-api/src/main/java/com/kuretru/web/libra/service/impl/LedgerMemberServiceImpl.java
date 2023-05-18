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
import com.kuretru.web.libra.entity.business.LedgerMemberBO;
import com.kuretru.web.libra.entity.data.LedgerMemberDO;
import com.kuretru.web.libra.entity.mapper.LedgerMemberEntityMapper;
import com.kuretru.web.libra.entity.query.LedgerMemberQuery;
import com.kuretru.web.libra.entity.transfer.LedgerMemberDTO;
import com.kuretru.web.libra.entity.view.LedgerMemberVO;
import com.kuretru.web.libra.mapper.LedgerMemberMapper;
import com.kuretru.web.libra.service.LedgerMemberService;
import com.kuretru.web.libra.service.LedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
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
public class LedgerMemberServiceImpl
        extends BaseServiceImpl<LedgerMemberMapper, LedgerMemberDO, LedgerMemberDTO, LedgerMemberQuery>
        implements LedgerMemberService {

    private final LedgerService ledgerService;

    @Autowired
    public LedgerMemberServiceImpl(LedgerMemberMapper mapper, LedgerMemberEntityMapper entityMapper,
                                   @Lazy LedgerService ledgerService) {
        super(mapper, entityMapper);
        this.ledgerService = ledgerService;
    }

    @Override
    public PaginationResponse<LedgerMemberVO> listVo(PaginationQuery pagination, LedgerMemberQuery query) {
        QueryWrapper<LedgerMemberBO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ledger_member.ledger_id", query.getLedgerId().toString());
        queryWrapper.orderByAsc("id");

        IPage<LedgerMemberBO> page = new Page<>(pagination.getCurrent(), pagination.getPageSize());
        page = mapper.listPageBo(page, queryWrapper);
        List<LedgerMemberVO> records = ((LedgerMemberEntityMapper)entityMapper).boToVo(page.getRecords());

        // TODO: 排序，Owner应该在最前
        return new PaginationResponse<>(records, page.getCurrent(), page.getSize(), page.getTotal());
    }

    @Override
    public void verifyIamLedgerMember(UUID ledgerId) throws ServiceException {
        QueryWrapper<LedgerMemberDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ledger_id", ledgerId.toString());
        queryWrapper.eq("user_id", AccessTokenContext.getUserId().toString());
        LedgerMemberDO record = mapper.selectOne(queryWrapper);
        if (record == null) {
            throw new ServiceException(UserErrorCodes.ACCESS_PERMISSION_ERROR, "用户非该账本成员");
        }
    }

    @Override
    public void verifyTheyAreLedgerMember(UUID ledgerId, Set<UUID> userIds) throws ServiceException {
        List<String> ids = userIds.stream().map(UUID::toString).toList();
        QueryWrapper<LedgerMemberDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ledger_id", ledgerId.toString());
        queryWrapper.in("user_id", ids);
        List<LedgerMemberDO> result = mapper.selectList(queryWrapper);
        if (result.size() != userIds.size()) {
            throw new ServiceException(UserErrorCodes.ACCESS_PERMISSION_ERROR, "部分用户不为该账本成员");
        }
    }

    @Override
    public List<LedgerMemberDTO> list() {
        throw new ServiceException(UserErrorCodes.MISSING_REQUIRED_PARAMETERS, "账本成员必须关联账本ID查询");
    }

    @Override
    public List<LedgerMemberDTO> list(LedgerMemberQuery query) throws ServiceException {
        List<LedgerMemberDTO> result = super.list(query);
        verifyListResult(result);
        return result;
    }

    @Override
    public PaginationResponse<LedgerMemberDTO> list(PaginationQuery paginationQuery) {
        throw new ServiceException(UserErrorCodes.MISSING_REQUIRED_PARAMETERS, "账本成员必须关联账本ID查询");
    }

    @Override
    public PaginationResponse<LedgerMemberDTO> list(PaginationQuery pagination, LedgerMemberQuery query) throws ServiceException {
        PaginationResponse<LedgerMemberDTO> result = super.list(pagination, query);
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
        throw new ServiceException(UserErrorCodes.ACCESS_PERMISSION_ERROR, "仅能查看自己所在账本的账本成员");
    }

    @Override
    protected void verifyCanGet(LedgerMemberDO record) throws ServiceException {
        // 只有该账本的成员可以查看该账本的成员
        if (UUID.fromString(record.getUserId()).equals(AccessTokenContext.getUserId())) {
            return;
        }
        verifyIamLedgerMember(UUID.fromString(record.getLedgerId()));
    }

    @Override
    protected void verifyCanRemove(LedgerMemberDO record) throws ServiceException {
        UUID myUserId = AccessTokenContext.getUserId();
        // 1. 自己可以主动退出某个账本
        // 2. 否则只有账本拥有者可以踢出某个成员
        boolean voluntarilyQuit = UUID.fromString(record.getUserId()).equals(myUserId);
        if (!voluntarilyQuit) {
            ledgerService.verifyIamLedgerOwner(UUID.fromString(record.getLedgerId()));

        }

        throw new ServiceException(ServiceErrorCodes.SYSTEM_NOT_IMPLEMENTED, "尚未实现该方法");
    }

    @Override
    protected void verifyDTO(LedgerMemberDTO record) throws ServiceException {
        ledgerService.verifyIamLedgerOwner(record.getLedgerId());
    }

    @Override
    protected LedgerMemberDTO findUniqueRecord(LedgerMemberDTO record) {
        QueryWrapper<LedgerMemberDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ledger_id", record.getLedgerId().toString());
        queryWrapper.eq("user_id", record.getUserId().toString());
        LedgerMemberDO result = mapper.selectOne(queryWrapper);
        return entityMapper.doToDto(result);
    }

}
