package com.kuretru.web.libra.service;

import com.kuretru.microservices.web.entity.PaginationQuery;
import com.kuretru.microservices.web.entity.PaginationResponse;
import com.kuretru.microservices.web.service.BaseService;
import com.kuretru.web.libra.entity.query.LedgerMemberQuery;
import com.kuretru.web.libra.entity.transfer.LedgerMemberDTO;
import com.kuretru.web.libra.entity.view.LedgerMemberVO;

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

}
