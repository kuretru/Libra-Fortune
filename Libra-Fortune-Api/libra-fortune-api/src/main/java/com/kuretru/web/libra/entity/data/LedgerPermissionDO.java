package com.kuretru.web.libra.entity.data;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kuretru.api.common.entity.data.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 账本用户权限表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("ledger_permission")
public class LedgerPermissionDO extends BaseDO {

    /**
     * 哪个用户
     */
    private String userId;

    /**
     * 对哪个账本
     */
    private String ledgerId;

    /**
     * 是否可修改这个账本
     */
    @TableField("is_writable")
    private Boolean writable;

    /**
     * 是否可读这个账本 逻辑删除
     */
    @TableField("is_readable")
    private Boolean readable;

}
