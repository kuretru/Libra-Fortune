package com.kuretru.web.libra.account.entity.mapper;

import com.kuretru.microservices.web.v2.entity.mapper.BaseEntityMapper;
import com.kuretru.web.libra.account.entity.data.AccountBalanceDO;
import com.kuretru.web.libra.account.entity.transfer.AccountBalanceDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountBalanceEntityMapper extends BaseEntityMapper<AccountBalanceDO, AccountBalanceDTO> {

}
