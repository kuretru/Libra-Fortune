package com.kuretru.web.libra.ledger.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.microservices.web.v2.entity.data.BaseCreateUpdateDO;
import com.kuretru.web.libra.ledger.entity.enums.EntryType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("ledger_v2_entry")
public class LedgerEntryDO extends BaseCreateUpdateDO {

    /** 外键，账本ID */
    private Long ledgerId;

    /** 分类路径，按层级以逗号分隔分类ID */
    private String category;

    /** 枚举值，条目类型 */
    private EntryType type;

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
