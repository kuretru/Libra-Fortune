package com.kuretru.web.libra.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.microservices.web.entity.data.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 支付渠道表
 *
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("payment_channel")
public class PaymentChannelDO extends BaseDO {

    /** 外键，归属用户ID */
    private String userId;

    /** 支付渠道名称 */
    private String name;

}
