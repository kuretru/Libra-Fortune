package com.kuretru.web.libra.ledger.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.microservices.web.v2.entity.data.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("ledger_v2_entry")
public class LedgerEntryDO extends BaseDO {

    /** 外键，账本ID */
    private Long ledgerId;

    /** 外键，分类ID */
    private Long categoryId;

    /** 交易日期 */
    private LocalDate date;

    /** 条目名称 */
    private String name;

    /** 原始消费金额 */
    private BigDecimal originalAmount;

    /** 原始消费货币 */
    private String originalCurrency;

    /** 结算金额 */
    private BigDecimal settlementAmount;

    /** 结算货币 */
    private String settlementCurrency;

    /** 备注 */
    private String remark;

}
