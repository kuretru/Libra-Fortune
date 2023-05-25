package com.kuretru.web.libra.service;

import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.BaseService;
import com.kuretru.web.libra.entity.query.LedgerEntryDetailTagQuery;
import com.kuretru.web.libra.entity.transfer.LedgerEntryDetailTagDTO;

import java.util.List;
import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
public interface LedgerEntryDetailTagService extends BaseService<LedgerEntryDetailTagDTO, LedgerEntryDetailTagQuery> {

    /**
     * 根据条目明细ID查询记录
     * 调用者需验证权限
     *
     * @param entryDetailId 条目明细ID
     * @return 记录
     */
    List<UUID> listByEntryDetailId(UUID entryDetailId);

    /**
     * 批量保存新记录
     * 调用者需保证已校验过数据
     *
     * @param entryDetailId 帐本条目明细ID
     * @param records       新记录
     * @return 保存后的新记录
     * @throws ServiceException 校验数据失败时会抛出业务异常
     */
    List<UUID> save(UUID entryDetailId, List<UUID> records) throws ServiceException;

    /**
     * 批量更新记录，必须传入所有字段
     * 调用者需保证已校验过数据
     *
     * @param entryDetailId 帐本条目明细ID
     * @param records       包含新数据及其他所有字段的记录
     * @return 更新后的新记录
     * @throws ServiceException 找不到指定记录时会抛出业务异常
     */
    List<UUID> update(UUID entryDetailId, List<UUID> records) throws ServiceException;


    /**
     * 根据账本条目明细ID列表删除所有标签记录
     *
     * @param entryDetailIds 账本条目明细ID列表
     */
    void removeByEntryDetailIds(List<UUID> entryDetailIds);

}
