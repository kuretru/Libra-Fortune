package com.kuretru.web.libra.account.entity.mapper;

import com.kuretru.microservices.web.v2.entity.mapper.BaseSequencedEntityMapper;
import com.kuretru.web.libra.account.entity.data.AccountDO;
import com.kuretru.web.libra.account.entity.transfer.AccountDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountEntityMapper extends BaseSequencedEntityMapper<AccountDO, AccountDTO> {

}
