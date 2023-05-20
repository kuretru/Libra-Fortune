package com.kuretru.web.libra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuretru.web.libra.entity.data.PaymentChannelDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Repository
@Mapper
public interface PaymentChannelMapper extends BaseMapper<PaymentChannelDO> {

    /**
     * 根据账本ID关联查询支出渠道
     *
     * @param ledgerId 账本ID
     * @return 支出渠道
     */
    List<PaymentChannelDO> listByLedgerId(@Param("ledgerId") String ledgerId);

}
