package com.kuretru.web.libra.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.microservices.web.entity.data.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

/**
 * 账本条目表
 *
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("ledger_entry")
public class LedgerEntryDO extends BaseDO {

    /** 外键，账本ID */
    private String ledgerId;

    /** 外键，条目分类ID */
    private String categoryId;

    /** 条目日期 */
    private LocalDate date;

    /** 条目名称 */
    private String name;

    /** 条目金额总计，扩大100倍存储 */
    private Long total;

    /** 货币类型 */
    private String currencyType;

    /** 备注 */
    private String remark;

}
