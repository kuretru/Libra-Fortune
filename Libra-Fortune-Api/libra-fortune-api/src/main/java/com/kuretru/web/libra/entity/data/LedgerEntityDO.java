package com.kuretru.web.libra.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuretru.api.common.entity.data.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;

/**
 * 账目
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("ledger_entity")
public class LedgerEntityDO extends BaseDO {

    /** 属于哪个账本 */
    private String ledgerId;

    /** 属于哪个大分类 */
    private String categoryId;

    /** 这笔账目的发生时间 */
    private String date;

    /** 账目的费用 */
    private Long amount;

    /** 备注 */
    private String remark;

}
