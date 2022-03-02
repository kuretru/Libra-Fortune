package com.kuretru.web.libra.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.api.common.entity.data.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 账本
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("ledger")
public class LedgerDO extends BaseDO {
    /** 属于哪个用户 */
    private String userId;

    /** 账本名称 */
    private String name;

    /** 账本备注 */
    private String remark;

}
