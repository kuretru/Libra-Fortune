package com.kuretru.web.libra.service;

import com.kuretru.microservices.web.entity.PaginationQuery;
import com.kuretru.microservices.web.entity.PaginationResponse;
import com.kuretru.microservices.web.service.BaseService;
import com.kuretru.web.libra.entity.query.LedgerEntryQuery;
import com.kuretru.web.libra.entity.transfer.LedgerEntryDTO;
import com.kuretru.web.libra.entity.view.LedgerEntryVO;

import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
public interface LedgerEntryService extends BaseService<LedgerEntryDTO, LedgerEntryQuery> {

    /**
     * 根据查询条件，分页查询所有记录
     *
     * @param pagination 分页参数
     * @param query      查询条件
     * @return 符合查询条件，分页后的所有记录
     */
    PaginationResponse<LedgerEntryVO> listVo(PaginationQuery pagination, LedgerEntryQuery query);

    /**
     * 按照账本分类统计记录条数
     * 调用者必须保证该用户具有查询该分类的权限
     *
     * @param categoryId 账本分类ID
     * @return 记录条数
     */
    long countByCategoryId(UUID categoryId);

}
