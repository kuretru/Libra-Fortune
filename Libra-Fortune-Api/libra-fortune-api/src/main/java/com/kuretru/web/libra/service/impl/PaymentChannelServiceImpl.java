package com.kuretru.web.libra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuretru.microservices.authentication.context.AccessTokenContext;
import com.kuretru.microservices.web.constant.code.ServiceErrorCodes;
import com.kuretru.microservices.web.constant.code.UserErrorCodes;
import com.kuretru.microservices.web.entity.PaginationQuery;
import com.kuretru.microservices.web.entity.PaginationResponse;
import com.kuretru.microservices.web.exception.ServiceException;
import com.kuretru.microservices.web.service.impl.BaseServiceImpl;
import com.kuretru.web.libra.entity.data.PaymentChannelDO;
import com.kuretru.web.libra.entity.mapper.PaymentChannelEntityMapper;
import com.kuretru.web.libra.entity.query.PaymentChannelQuery;
import com.kuretru.web.libra.entity.transfer.PaymentChannelDTO;
import com.kuretru.web.libra.entity.view.PaymentChannelVO;
import com.kuretru.web.libra.mapper.PaymentChannelMapper;
import com.kuretru.web.libra.service.PaymentChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public Map<UUID, List<PaymentChannelVO>> listMapByLedgerId(UUID ledgerId) {
        List<PaymentChannelDO> records = mapper.listByLedgerId(ledgerId.toString());
        Map<UUID, List<PaymentChannelVO>> result = new HashMap<>(16);
        records.forEach(record -> {
            UUID userId = UUID.fromString(record.getUserId());
            if (!result.containsKey(userId)) {
                result.put(userId, new ArrayList<>());
            }
            result.get(userId).add(((PaymentChannelEntityMapper)entityMapper).doToVo(record));
        });
        return result;
    }

    @Override
    public List<PaymentChannelDTO> list() {
        throw new ServiceException(UserErrorCodes.MISSING_REQUIRED_PARAMETERS, "支出渠道必须关联用户ID查询");
    }

    @Override
    public PaginationResponse<PaymentChannelDTO> list(PaginationQuery paginationQuery) {
        throw new ServiceException(UserErrorCodes.MISSING_REQUIRED_PARAMETERS, "支出渠道必须关联用户ID查询");
    }

    @Override
    protected void verifyCanGet(PaymentChannelDO record) throws ServiceException {
        boolean notMe = !UUID.fromString(record.getUserId()).equals(AccessTokenContext.getUserId());
        if (notMe) {
            throw new ServiceException(UserErrorCodes.ACCESS_PERMISSION_ERROR, "仅能查看自己的支出渠道");
        }
    }

    @Override
    protected void verifyCanRemove(PaymentChannelDO record) throws ServiceException {
        throw new ServiceException(ServiceErrorCodes.SYSTEM_NOT_IMPLEMENTED, "暂时无法删除支出渠道");
    }

    @Override
    protected void verifyQuery(PaymentChannelQuery query) throws ServiceException {
        boolean notMe = !query.getUserId().equals(AccessTokenContext.getUserId());
        if (notMe) {
            throw new ServiceException(UserErrorCodes.REQUEST_PARAMETER_ERROR, "仅能查看自己的支出渠道");
        }
    }

    @Override
    protected void verifyDTO(PaymentChannelDTO record) throws ServiceException {
        boolean notMe = !record.getUserId().equals(AccessTokenContext.getUserId());
        if (notMe) {
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
