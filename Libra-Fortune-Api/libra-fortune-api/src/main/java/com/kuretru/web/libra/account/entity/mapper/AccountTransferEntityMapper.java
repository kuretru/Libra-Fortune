package com.kuretru.web.libra.account.entity.mapper;

import com.kuretru.microservices.web.entity.mapper.BaseEntityMapper;
import com.kuretru.web.libra.account.entity.data.AccountTransferDO;
import com.kuretru.web.libra.account.entity.transfer.AccountTransferDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountTransferEntityMapper extends BaseEntityMapper<AccountTransferDO, AccountTransferDTO> {

}
