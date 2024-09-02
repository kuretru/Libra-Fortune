package com.kuretru.web.libra.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.microservices.web.entity.data.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 账本成员关联表
 *
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("ledger_member")
public class LedgerMemberDO extends BaseDO {

    /** 外键，账本ID */
    private String ledgerId;

    /** 外键，用户ID */
    private String userId;

    /** 默认承担比例，保留2位小数，扩大100倍存储 */
    private Short defaultFundedRatio;

}
