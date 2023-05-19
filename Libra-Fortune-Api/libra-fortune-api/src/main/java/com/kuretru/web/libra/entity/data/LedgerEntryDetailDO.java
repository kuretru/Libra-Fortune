package com.kuretru.web.libra.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.microservices.web.entity.data.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 账本条目详细表
 *
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("ledger_entry_detail")
public class LedgerEntryDetailDO extends BaseDO {

    /** 外键，条目ID */
    private String entryId;

    /** 外键，归属用户ID */
    private String userId;

    /** 外键，支出渠道ID */
    private String paymentChannelId;

    /** 承担比例，保留2位小数，扩大100倍存储 */
    private Short fundedRatio;

    /** 承担金额，扩大100倍存储 */
    private Long amount;

}
