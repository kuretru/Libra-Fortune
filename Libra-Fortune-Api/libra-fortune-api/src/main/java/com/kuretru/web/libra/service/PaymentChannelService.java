package com.kuretru.web.libra.service;

import com.kuretru.microservices.web.service.BaseService;
import com.kuretru.web.libra.entity.query.PaymentChannelQuery;
import com.kuretru.web.libra.entity.transfer.PaymentChannelDTO;
import com.kuretru.web.libra.entity.view.PaymentChannelVO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
public interface PaymentChannelService extends BaseService<PaymentChannelDTO, PaymentChannelQuery> {

    /**
     * 根据账本ID，构建用户ID->支出渠道的Map
     *
     * @param ledgerId 账本ID
     * @return 用户ID->支出渠道的Map
     */
    Map<UUID, List<PaymentChannelVO>> listMapByLedgerId(UUID ledgerId);

}
