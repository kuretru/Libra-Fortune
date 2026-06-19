package com.kuretru.web.libra.ledger.service;

import com.kuretru.microservices.web.v2.entity.query.EmptyQuery;
import com.kuretru.microservices.web.v2.service.BaseService;
import com.kuretru.microservices.web.v2.service.ChildrenCapable;
import com.kuretru.web.libra.ledger.entity.transfer.LedgerMemberDTO;

public interface LedgerMemberService extends BaseService<LedgerMemberDTO, EmptyQuery>, ChildrenCapable<LedgerMemberDTO> {

}
