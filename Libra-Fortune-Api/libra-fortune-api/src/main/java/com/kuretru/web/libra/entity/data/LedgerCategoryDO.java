package com.kuretru.web.libra.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.microservices.web.entity.data.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 账本条目分类表
 *
 * @author 呉真(kuretru) <kuretru@gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("ledger_category")
public class LedgerCategoryDO extends BaseDO {

    /** 外键，账本ID */
    private String ledgerId;

    /** 分类名称 */
    private String name;

}
