package com.kuretru.web.libra.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.api.common.entity.data.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


/**
 * 账目的主分类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("ledger_category")
public class LedgerCategoryDO extends BaseDO {

    /** 属于哪个账本 */
    private String ledgerId;

    /** 大分类的名称 */
    private String name;

}
