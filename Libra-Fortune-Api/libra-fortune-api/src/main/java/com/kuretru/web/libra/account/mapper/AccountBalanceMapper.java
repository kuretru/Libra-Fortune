package com.kuretru.web.libra.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuretru.web.libra.account.entity.data.AccountBalanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AccountBalanceMapper extends BaseMapper<AccountBalanceDO> {
}
