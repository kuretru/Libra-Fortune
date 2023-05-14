package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.authentication.context.AccessTokenContext;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.PaymentChannelDO;
import com.kuretru.web.libra.entity.mapper.PaymentChannelEntityMapper;
import com.kuretru.web.libra.entity.query.PaymentChannelQuery;
import com.kuretru.web.libra.entity.transfer.PaymentChannelDTO;
import com.kuretru.web.libra.mapper.PaymentChannelMapper;
import com.kuretru.web.libra.service.PaymentChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Service
public class PaymentChannelServiceImpl
        extends BaseServiceImpl<PaymentChannelMapper, PaymentChannelDO, PaymentChannelDTO, PaymentChannelQuery>
        implements PaymentChannelService {

    @Autowired
    public PaymentChannelServiceImpl(PaymentChannelMapper mapper, PaymentChannelEntityMapper entityMapper) {
        super(mapper, entityMapper);
    }

    @Override
    protected void verifyDTO(PaymentChannelDTO record) throws ServiceException {
        if (!record.getUserId().equals(AccessTokenContext.getUserId())) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "只有本人可以修改支出渠道");
        }
    }

    @Override
    protected PaymentChannelDTO findUniqueRecord(PaymentChannelDTO record) {
        QueryWrapper<PaymentChannelDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", record.getUserId());
        queryWrapper.eq("name", record.getName());
        PaymentChannelDO result = mapper.selectOne(queryWrapper);
        return entityMapper.doToDto(result);
    }

}
