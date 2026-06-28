package com.kuretru.web.libra.account.mapper;

import com.kuretru.microservices.web.mapper.BaseSequencedMapper;
import com.kuretru.web.libra.account.entity.data.AccountDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AccountMapper extends BaseSequencedMapper<AccountDO> {
}
