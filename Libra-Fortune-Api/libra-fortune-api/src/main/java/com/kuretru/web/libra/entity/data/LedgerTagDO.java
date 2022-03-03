package com.kuretru.web.libra.entity.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.api.common.entity.data.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 标签名称，并且与用户关联
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("ledger_tag")
public class LedgerTagDO extends BaseDO {
    /**
     * 哪个用户
     */
    private String userId;
    /**
     * 标签名称
     */
    private String name;

}
