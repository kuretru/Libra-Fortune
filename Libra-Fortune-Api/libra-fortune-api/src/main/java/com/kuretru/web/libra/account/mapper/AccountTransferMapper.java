package com.kuretru.web.libra.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuretru.web.libra.account.entity.data.AccountTransferDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AccountTransferMapper extends BaseMapper<AccountTransferDO> {

}
