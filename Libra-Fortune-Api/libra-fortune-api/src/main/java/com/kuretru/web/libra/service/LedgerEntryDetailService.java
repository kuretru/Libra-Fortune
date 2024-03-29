package com.kuretru.web.libra.service;

import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.BaseService;
import com.kuretru.web.libra.entity.query.LedgerEntryDetailQuery;
import com.kuretru.web.libra.entity.transfer.LedgerEntryDetailDTO;

import java.util.List;
import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
public interface LedgerEntryDetailService extends BaseService<LedgerEntryDetailDTO, LedgerEntryDetailQuery> {

    /**
     * 根据条目ID查询记录
     * 调用者需验证权限
     *
     * @param entryId 条目ID
     * @return 记录
     */
    List<LedgerEntryDetailDTO> listByEntryId(UUID entryId);

    /**
     * 批量保存新记录
     * 调用者需保证已校验过数据
     *
     * @param entryId 账本条目ID
     * @param records 新记录
     * @return 保存后的新记录
     * @throws ServiceException 校验数据失败时会抛出业务异常
     */
    List<LedgerEntryDetailDTO> save(UUID entryId, List<LedgerEntryDetailDTO> records) throws ServiceException;

    /**
     * 批量更新记录，必须传入所有字段
     * 调用者需保证已校验过数据
     *
     * @param entryId 账本条目ID
     * @param records 包含新数据及其他所有字段的记录
     * @return 更新后的新记录
     * @throws ServiceException 找不到指定记录时会抛出业务异常
     */
    List<LedgerEntryDetailDTO> update(UUID entryId, List<LedgerEntryDetailDTO> records) throws ServiceException;

    /**
     * 根据账本条目ID删除所有明细记录
     *
     * @param entryId 账本条目ID
     * @throws ServiceException 找不到指定记录时会抛出业务异常
     */
    void removeByEntryId(UUID entryId) throws ServiceException;

}
