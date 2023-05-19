package com.kuretru.web.libra.service;

import com.kuretru.microservices.web.entity.PaginationQuery;
import com.kuretru.microservices.web.entity.PaginationResponse;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.BaseService;
import com.kuretru.web.libra.entity.query.LedgerQuery;
import com.kuretru.web.libra.entity.transfer.LedgerDTO;
import com.kuretru.web.libra.entity.view.LedgerVO;

import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
public interface LedgerService extends BaseService<LedgerDTO, LedgerQuery> {

    /**
     * 不鉴权，直接根据UUID查询账本
     *
     * @param uuid UUID
     * @return 账本实体
     */
    LedgerDTO getDirect(UUID uuid);

    /**
     * 根据查询条件，分页查询所有记录
     *
     * @param pagination 分页参数
     * @param query      查询条件
     * @return 符合查询条件，分页后的所有记录
     */
    PaginationResponse<LedgerVO> listVo(PaginationQuery pagination, LedgerQuery query);

    /**
     * 验证当前用户是否是指定账本的拥有者
     *
     * @param ledgerId 账本ID
     * @throws ServiceException 不是拥有者时引发业务异常
     */
    void verifyIamLedgerOwner(UUID ledgerId) throws ServiceException;

}
