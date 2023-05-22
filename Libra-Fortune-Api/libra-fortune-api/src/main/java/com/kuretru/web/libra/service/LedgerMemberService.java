package com.kuretru.web.libra.service;

import com.kuretru.microservices.web.entity.PaginationQuery;
import com.kuretru.microservices.web.entity.PaginationResponse;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.BaseService;
import com.kuretru.web.libra.entity.query.LedgerMemberQuery;
import com.kuretru.web.libra.entity.transfer.LedgerMemberDTO;
import com.kuretru.web.libra.entity.view.LedgerMemberVO;
import com.kuretru.web.libra.entity.view.UserVO;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
public interface LedgerMemberService extends BaseService<LedgerMemberDTO, LedgerMemberQuery> {

    /**
     * 根据查询条件，分页查询所有记录
     *
     * @param pagination 分页参数
     * @param query      查询条件
     * @return 符合查询条件，分页后的所有记录
     */
    PaginationResponse<LedgerMemberVO> listVo(PaginationQuery pagination, LedgerMemberQuery query);

    /**
     * 根据账本ID查询VO
     * 调用者需确保已进行过权限验证
     *
     * @param ledgerId 账本ID
     * @return VO
     */
    Map<UUID, UserVO> listUserMapByLedgerId(UUID ledgerId);

    /**
     * 验证当前用户是否为指定账本成员
     *
     * @param ledgerId 账本ID
     * @throws ServiceException 不是账本成员时，返回业务异常
     */
    void verifyIamLedgerMember(UUID ledgerId) throws ServiceException;

    /**
     * 验证传入的用户ID列表是否为指定账本成员
     *
     * @param ledgerId 账本ID
     * @param userIds  用户ID列表
     * @throws ServiceException 不是账本成员时，返回业务异常
     */
    void verifyTheyAreLedgerMember(UUID ledgerId, Set<UUID> userIds) throws ServiceException;

}
