package com.kuretru.web.libra.ledger.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.microservices.web.v2.entity.data.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("ledger_v2_member")
public class LedgerMemberDO extends BaseDO {

    /** 外键，账本ID */
    private Long ledgerId;

    /** 成员用户名 */
    private String username;

    /** 百分数，默认承担比例 */
    private BigDecimal defaultFundedRatio;

}
